# recommend_api.py
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List, Dict, Optional
import numpy as np
import pandas as pd
from scipy.sparse import csr_matrix
from sklearn.neighbors import NearestNeighbors

app = FastAPI(title="B2B Gas Portal Recommendation API")

# Pydantic models for request validation
class OrderItem(BaseModel):
    client_id: int
    product_id: int
    quantity: Optional[int] = 1  # default 1 if not provided

class RecommendationRequest(BaseModel):
    orders: List[OrderItem]
    query_client_id: int
    top_k: Optional[int] = 5


@app.post("/recommend")
def recommend(data: RecommendationRequest):
    orders_list = data.orders
    query_client_id = data.query_client_id
    top_k = data.top_k

    # Build DataFrame from input orders
    if not orders_list:
        raise HTTPException(status_code=400, detail="Orders list cannot be empty.")

    rows = [(o.client_id, o.product_id, o.quantity or 1) for o in orders_list]
    orders_df = pd.DataFrame(rows, columns=['client_id', 'product_id', 'quantity'])

    # Build mapping
    clients = orders_df['client_id'].unique()
    products = orders_df['product_id'].unique()
    client_to_idx = {c: i for i, c in enumerate(clients)}
    product_to_idx = {p: i for i, p in enumerate(products)}

    # Make sparse matrix
    rows_ind = orders_df['client_id'].map(client_to_idx).to_numpy()
    cols_ind = orders_df['product_id'].map(product_to_idx).to_numpy()
    data_arr = np.ones(len(rows_ind))  # binary matrix
    X_bin = csr_matrix((data_arr, (rows_ind, cols_ind)), shape=(len(clients), len(products)))

    # Train item-based nearest neighbors
    item_vectors = X_bin.T
    max_neighbors = 5
    n_samples = item_vectors.shape[0]
    n_neighbors = min(max_neighbors, n_samples - 1) if n_samples > 1 else 1
    nn = NearestNeighbors(n_neighbors=n_neighbors + 1, metric='cosine', algorithm='brute')
    nn.fit(item_vectors)

    distances, indices = nn.kneighbors(item_vectors)
    item_neighbors = {}
    for item_idx in range(item_vectors.shape[0]):
        neighs = indices[item_idx][1:]
        sims = 1 - distances[item_idx][1:]
        item_neighbors[item_idx] = list(zip(neighs, sims))

    idx_to_product = {v: k for k, v in product_to_idx.items()}

    # Recommendation function
    if query_client_id not in client_to_idx:
        return {"client_id": query_client_id, "recommendations": []}  # no data for client

    uidx = client_to_idx[query_client_id]
    user_row = X_bin.getrow(uidx)
    bought_items = [(i, 1.0) for i in user_row.indices]

    scores = np.zeros(X_bin.shape[1], dtype=float)
    for item_idx, weight in bought_items:
        for neigh_idx, sim in item_neighbors[item_idx]:
            scores[neigh_idx] += weight * sim

    scores[user_row.indices] = 0.0  # remove already bought
    top_idx = np.argsort(scores)[::-1][:top_k]
    rec_products = [{"product_id": int(idx_to_product[i]), "score": float(scores[i])}
                    for i in top_idx if scores[i] > 0]

    return {"client_id": query_client_id, "recommendations": rec_products}
