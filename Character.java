public class Character
{
    private String name;
    private int xp;
    private int level;
    private double hp;
    private double mana;
    private double mmana;
    private double mHp;
    private double atk;
    private double def;
    private double matk;
    private double mult;
    private double regenrate;
    private Class type;
    private Race race;
    private boolean isAlive;
    private boolean isImmortal=false;
    private boolean isInvincible=false;
    public Character(String n,Class c,Race r,double m) {
        name=n;
        type=c;
        mult=m;
        race=r;
        setRegenRate(0.01);
        calcStats();
    }
    
    public int getLevel() {
        calcStats();
        return level;
    }

    public int getXP() {
        return xp;
    }

    public boolean isInvincible() {
        return isInvincible;
    }

    public boolean isImmortal() {
        return isImmortal;
    }

    public void invincible(boolean i) {
        isInvincible=i;
    }

    public void immortal(boolean i) {
        isImmortal=i;
        if(i) {
            heal();
            setMana(mmana);
        }
    }

    public void regen() {
        double heal=mHp*regenrate;
        heal*=race.getRegenMult();
        heal(heal);
    }

    public String toString() {
        return "Name: "+name+"\n"+
        "XP: "+xp+"\n"+
        "Level: "+level+"\n\n"+
        "Race: \n"+race+"\n"+
        "Class: \n"+type+"\n"+
        "\nHp: "+hp+"\n"+
        "Mana: "+mana+"\n"+
        "Max Mana: "+mmana+"\n"+
        "Max hp: "+mHp+"\n"+
        "Attack: "+atk+"\n"+
        "Defense: "+def+"\n"+
        "Magic Attack: "+matk+"\n"+
        "Mult: "+mult+"\n"+
        "Regen Rate: "+regenrate*race.getRegenMult()+"\n"+
        "Is Alive: "+isAlive+"\n"+
        "Is Invincible: "+isInvincible+"\n"+
        "Is Immortal: "+isImmortal+"\n";
    }

    public Class getType() {
        return type;
    }

    public Race getRace() {
        return race;
    }

    public double getHp() {
        return hp;
    }

    public double getMana() {
        return mana;
    }

    public void subMana(double m) {
        if(!isImmortal) {
            mana-=m;
            if(mana<0) {
                mana=0;
            }
        }
    }

    public void regenMana(double a) {
        mana+=a;
        if(mana>mmana) {
            mana=mmana;
        }
    }

    public void regenMana() {
        regenMana(mmana/30);
    }

    public void setMana(double a) {
        mana=a;
        if(mana>mmana) {
            mana=mmana;
        }
    }

    public double getMaxMana() {
        return mmana;
    }

    public double getMaxHp() {
        return mHp;   
    }

    public double getAtk() {
        return atk;
    }

    public double getDef() {
        return def;
    }

    public double getMAtk() {
        return matk;
    }

    public double getRegenRate() {
        return regenrate;
    }

    public void setRegenRate(double r) {
        regenrate=r;
    }

    public void heal(double n) {
        hp+=n;
        if(hp>mHp) {
            hp=mHp;
        }
    }

    public void heal() {
        hp=mHp;
    }

    public void damage(double n) {
        if(!isInvincible && !isImmortal) {
            hp-=n;
            if(hp<=0) {
                hp=0;
            }
        }
    }

    public void die() {
        if(!isInvincible && !isImmortal) {
            isAlive=false;
            hp=0;
            mana=0;
        }
    }

    public void revive() {
        isAlive=true;
        hp=mHp;
        mana=mmana;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setClass(Class t) {
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
        try{
            calcLevel();
            calcAtk();
            calcDef();
            calcMAtk();
            calcHp();
            calcMana();
        }catch(Exception e) {

        }
    }

    public void calcMana() {
        mmana=type.getBMana()*Math.pow(mult,level);
        mmana*=race.getManaMult();
    }

    public void calcHp() {
        mHp=type.getBHp()*Math.pow(mult,level);
        mHp*=race.getHpMult();
    }

    public void calcAtk() {
        atk=type.getBAtk()*Math.pow(mult,level);
        atk*=race.getAtkMult();
    }

    public void calcDef() {
        def=type.getBDef()*Math.pow(mult,level);
        def*=race.getDefMult();
    }

    public void calcMAtk() {
        matk=type.getBMatk()*Math.pow(mult,level);
        matk*=race.getMAtkMult();
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