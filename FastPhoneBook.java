package oy.tol.tra;

public class FastPhoneBook implements PhoneBook {

    static final int MAX_TABLE_SIZE = 10000000;
    private Person [] persons = new Person[MAX_TABLE_SIZE];
    private int personCount = 0;
    int hashindex = 0;
    int collisions = 0;
    int steps = 0;
    /**
     * TODO: Implement the add depending on your selected data structure; either hash table or BST.
     * In both cases, you need to calculate the hash code in the Person class.
     * 
     * @param person The person to add to the phone book.
     * @return True if managed to add the person, otherwise false
     */
    @Override
    public boolean add(Person person) throws IllegalArgumentException {
        if (null == person) throw new IllegalArgumentException("Person cannot be null");
        int i = 0;
        while(i < persons.length - 1){
            hashindex = person.hashCode();
            hashindex = (hashindex + i & 0x7fffffff) % MAX_TABLE_SIZE;
            if (persons[hashindex] == null){
                persons[hashindex] = person;
                personCount++;
                return true;
            }
            i++;
            if(steps < i){
                steps = i;
            }
            collisions++;
        }

        return false;
    }

    @Override
    public int size() {
        return personCount;
    }

    /**
     * TODO: Implement the search function.
     * Implementation depends on which data structure you use, either hash table or BST.
     *
     * @param number The phone number to search from the phone book.
     * @return Return the Person object, or if not found null.
     */
    @Override
    public Person findPersonByPhone(String number) throws IllegalArgumentException {
        if (null == number) throw new IllegalArgumentException("Phone number cannot be null");
        int i = 0;
        Person search = new Person(number);
        hashindex = search.hashCode();
        hashindex = (hashindex & 0x7fffffff) % MAX_TABLE_SIZE;
        while(i < persons.length -1 && persons[hashindex] != null){
            if(number.equals(persons[hashindex].getPhoneNumber())){
                return persons[hashindex];
            }
            i++;
            if(i < persons.length -1){
                hashindex = (hashindex + i & 0x7fffffff) % MAX_TABLE_SIZE;
            }
        }
         
        
        return null;
  
    }

	@Override
	public Person[] getPersons() {
        // Students: You do not need to implemented this here.
        // Just let it return null.
		return null;
	}

    /**
     * Prints out the statistics of the phone book.
     * Here you should print out member variable information which tell something about
     * your implementation.
     * <p>
     * For example, if you implement this using a hash table, update member variables of the class
     * (int counters) in add(Person) whenever a collision happen. Then print this counter value here. 
     * You will then see if you have too many collisions. It will tell you that your hash function
     * is not good. In the teacher's implementation, there were 864 collisions and when using 
     * linear probing to handle the collision, max number of steps to find a free slot
     * in the table was 2. This is not too bad.
     */
    @Override
    public void printStatus() {
        System.out.println("Collisions:" + collisions + "Max steps:"  + steps);
    }
    
}
