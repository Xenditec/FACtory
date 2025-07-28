package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class DishesFactory {
        public static Dishes_item createDish(DishesType type) {
            switch (type) {
                case BANANA: return new Drinks.CoktailBanana();
                case LIME: return new Drinks.LaimJuce();
                case STRAWBERRY: return new Drinks.SmusiStrawberry();
                case KIWI: return new Drinks.KiwiDrink();
                default: throw new IllegalArgumentException("Неизвестный тип блюда: " + type);
            }
        }
    }
