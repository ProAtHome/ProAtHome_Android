package com.proathome.Utils;

import com.proathome.Utils.pojos.Component;
import com.proathome.Utils.pojos.ComponentTicket;
import com.proathome.Utils.pojos.ComponentProfesional;
import com.proathome.Utils.pojos.ComponentSesionesProfesional;

public interface OnClickListener {
    void onClick(Component component);
    void onClickGestionarProfesional(ComponentProfesional componentProfesional);
    void onClickProfesional(ComponentSesionesProfesional component);
    void onClickTicket(ComponentTicket componentTicket);
}
