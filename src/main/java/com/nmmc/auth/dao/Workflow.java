package com.nmmc.auth.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Workflow {

    private String templateName;
    
    private List<WorkflowDetails> workflowDetails;
}

