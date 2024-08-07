package com.workdance.chatbot.ui.main.my;

import lombok.Data;

@Data
public class ServiceItem {
    private String name;
    private int icon;
    private ServiceItemType code;

    public ServiceItem(int icon, String name, ServiceItemType code) {
        this.name = name;
        this.icon = icon;
        this.code = code;
    }

    public enum ServiceItemType {
        Pay(0),
        Wallet(1),
        Shop(2),
        Sport(3),
        See(4),
        Travel(5),
        Shake(6),
        Movie(7),
        Music(8),
        Game(9),
        Book(10),
        Other(11);

        public final int value;

        ServiceItemType(int i) {
            this.value = i;
        }
    }
}
