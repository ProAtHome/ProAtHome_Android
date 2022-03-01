package com.proathome.Interfaces.fragments_compartidos.TicketAyuda;

import org.json.JSONArray;

public interface TicketAyudaView {

    void showProgress();

    void hideProgress();

    void ticketFinalizado();

    void resetAdapter();

    void setAdapterMsg(JSONArray jsonArray);

}
