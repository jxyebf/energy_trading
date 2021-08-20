package com.example.energy_trading.icon;

import com.joanzapata.iconify.Icon;


public enum EcIcons implements Icon {
    icon_scan('\ue602'),//EcIcons.values()
    icon_ali_pay('\ue606'),
    icon_star('\ue66e'),
    icon_selected_star('\ue805');


    private char character;

    EcIcons(char character) {
        this.character = character;
    }

    @Override
    public String key() {
        return name().replace('_', '-');
    }

    @Override
    public char character() {
        return character;
    }
}
