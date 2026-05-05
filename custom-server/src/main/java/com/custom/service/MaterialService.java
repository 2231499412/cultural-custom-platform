package com.custom.service;

import com.custom.common.PageResult;
import com.custom.dto.MaterialQueryDTO;
import com.custom.entity.Material;

public interface MaterialService {

    PageResult<Material> listMaterials(MaterialQueryDTO query);
}
