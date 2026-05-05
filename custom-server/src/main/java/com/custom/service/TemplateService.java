package com.custom.service;

import com.custom.common.PageResult;
import com.custom.dto.TemplateQueryDTO;
import com.custom.entity.Template;

public interface TemplateService {

    PageResult<Template> listTemplates(TemplateQueryDTO query);

    Template getTemplateById(Long id);
}
