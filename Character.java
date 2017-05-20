public class Character
{
    private String name;
    private int xp;
    private int level;
    private int hp;
    private int mana;
    private int mHp;
    private int atk;
    private int def;
    private int matk;
    private String type;
    public Character(String n,String t) {
        name=n;
        type=t;
    }

    public void setType(String t) {
        type=t;
    }

    public void setXp(int x) {
        xp=x;
        checkXp();
    }

    public void addXp(int x) {
        xp+=x;
        checkXp();
    }

    public void calcStats() {
        calcLevel();
        calcAtk();
        calcDef();
        calcMAtk();
        calcHp();
        calcMana();
    }
    
    public void calcMana() {
        if(type.equals("Regular")) {
            mHp=(int) (30*Math.pow(1.1,level));
        }
        if(type.equals("Warrior")) {
            mHp=(int) (5*Math.pow(1.1,level));
        }
        if(type.equals("Guardian")) {
            mHp=(int) (5*Math.pow(1.1,level));
        }
        if(type.equals("Titan")) {
            mHp=(int) (400*Math.pow(1.1,level));
        }
    }

    public void calcHp() {
        if(type.equals("Regular")) {
            mHp=(int) (100*Math.pow(1.1,level));
        }
        if(type.equals("Warrior")) {
            mHp=(int) (120*Math.pow(1.1,level));
        }
        if(type.equals("Guardian")) {
            mHp=(int) (160*Math.pow(1.1,level));
        }
        if(type.equals("Titan")) {
            mHp=(int) (1200*Math.pow(1.1,level));
        }
    }

    public void calcAtk() {
        if(type.equals("Regular")) {
            atk=(int) (25*Math.pow(1.1,level));
        }
        if(type.equals("Warrior")) {
            atk=(int) (30*Math.pow(1.1,level));
        }
        if(type.equals("Guardian")) {
            atk=(int) (20*Math.pow(1.1,level));
        }
        if(type.equals("Titan")) {
            atk=(int) (280*Math.pow(1.1,level));
        }
    }

    public void calcDef() {
        if(type.equals("Regular")) {
            def=(int) (25*Math.pow(1.1,level));
        }
        if(type.equals("Warrior")) {
            def=(int) (30*Math.pow(1.1,level));
        }
        if(type.equals("Guardian")) {
            def=(int) (50*Math.pow(1.1,level));
        }
        if(type.equals("Titan")) {
            def=(int) (300*Math.pow(1.1,level));
        }
    }

    public void calcMAtk() {
        if(type.equals("Regular")) {
            matk=(int) (25*Math.pow(1.1,level));
        }
        if(type.equals("Warrior")) {
            matk=(int) (10*Math.pow(1.1,level));
        }
        if(type.equals("Guardian")) {
            matk=(int) (8*Math.pow(1.1,level));
        }
        if(type.equals("Titan")) {
            matk=(int) (320*Math.pow(1.1,level));
        }
    }

    public void calcLevel() {
        int lvl = (int)Math.sqrt(Math.sqrt(xp));
        if(lvl<1) {
            lvl=1;
        }
        level=lvl;
        checkXp();
    }
    
    public void checkXp() {
        if(xp>100000000) {
            xp=100000000;
        }
    }
}