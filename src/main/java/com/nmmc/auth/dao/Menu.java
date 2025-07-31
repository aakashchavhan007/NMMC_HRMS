
package com.nmmc.auth.dao;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    public UUID menuId;
    public String menuNameEng;
    public String menuNameMr;
    public String url;
    public String menuFlag;
    public UUID moduleId;
    public String moduleName;

}
