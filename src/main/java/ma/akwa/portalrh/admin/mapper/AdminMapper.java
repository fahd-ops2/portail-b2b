package ma.akwa.portalrh.admin.mapper;

import ma.akwa.portalrh.admin.dto.AdminRequest;
import ma.akwa.portalrh.admin.dto.AdminResponse;
import ma.akwa.portalrh.admin.entities.Admin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    Admin toEntity(AdminRequest request);
    AdminResponse toResponse(Admin admin);
}
