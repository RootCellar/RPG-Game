public class Tester
{
    public static void main(String args[]) {
        Race race = new Race("Human",1,1,1,1,1,1);
        Race race2 = new Race("Giant",1.5,1.5,1.5,1.5,0,0);
        Race race3 = new Race("Titan",5,5,5,5,5,5);
        Class Class = new Class("Warrior",100,0,35,35,0);
        Class Class2 = new Class("Mage",70,30,15,15,45);
        Character ch = new Character("Darian",Class2,race,1.1);
        ch.setXp(100000000);
        ch.calcStats();
        System.out.println(ch);
    }
    
    /**
 	* Calculates the damage done
 	* @param damage attack stat of the attacker
 	* @param armor defense stat of the person being attacked
 	* @return The damage done as a double
 	* This damage system relies on the relation of the attack power to the defense of the person being attacked.
 	* If the attack power is half of the defense, the damage is halved.
 	* If the attack power is one tenth of the defense, the attack is blocked, and does 0 damage.
 	*/
	public static double CalcDamage(int damage, int armor) {
    	double a=damage; //A is the damage, b is the armor of the person being attacked
    	double b=armor;
    	double c=a/b; //Finds the multiplier
    	double finalDamage;
    	if(a>b) { //Reduces damage multiplier if the attack is greater than the defense
        	c=c-1;
        	c=c/3.0;
        	c=c+1;
    	}
    	if(c<=0.1) { //if the attack does very low damage, it does 0
        	return 0;
    	}
    	else {
        	finalDamage=a*c; //Uses the multiplier to calculate and return the damage done
        	return finalDamage;
    	}
	}
}