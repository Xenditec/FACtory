package org.example;


public class Drinks {
    public static class CoktailBanana implements Dishes_item{

        @Override
        public String toString(){
            return getName();
        }

        @Override
        public String getName() {
            return "Банановый коктейль";
        }

    }

    public static class LaimJuce implements Dishes_item{

        @Override
        public String toString(){
            return getName();
        }

        @Override
        public String getName() {
            return "Лаймовый сок";
        }
    }

    public static class SmusiStrawberry implements Dishes_item {

        @Override
        public String toString(){
            return getName();
        }

        @Override
        public String getName() {
            return "Вкуснейший клубничный смузи";
        }
    }

    public static class KiwiDrink implements Dishes_item{
        @Override
        public  String toString(){
            return getName();
        }
        @Override
        public String getName(){
            return "Коктейль с киви";
        }
    }


}
