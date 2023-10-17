import java.util.Random;

class Dice {
    private int sides;
    private int faceValue;
    private boolean isSelected;
    private Random random;

    public Dice(int sides) {
        this.sides = sides;
        this.isSelected = false;
        this.random = new Random();
        roll();
    }

    public int roll() {
        if (!isSelected) {
            faceValue = random.nextInt(sides) + 1;
        }
        return faceValue;
    }

    public int getFaceValue() {
        return faceValue;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
