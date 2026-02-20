# Use official Node image
FROM node:20-alpine

# Set working directory
WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy app source code
COPY . .

# Expose app port (change if needed)
EXPOSE 8089

# Start the app
CMD ["npm", "run", "dev"]