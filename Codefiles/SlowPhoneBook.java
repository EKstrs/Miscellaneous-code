package oy.tol.tra;

/**
 * A slow phonebook using linear search with phone number.
 */
public class SlowPhoneBook implements PhoneBook {

   static final int MAX_TABLE_SIZE = 10000000;
   private Person [] persons = new Person[MAX_TABLE_SIZE];
   private int personCount = 0;

   @Override
   public boolean add(Person person) throws IllegalArgumentException {
      if (null == person) throw new IllegalArgumentException("Person cannot be null");
      if (personCount < MAX_TABLE_SIZE - 1 ) {
         persons[personCount++] = person;
         return true;
      }
      return false;
   }

   @Override
   public int size() {
      return personCount;
   }

   @Override
   public Person findPersonByPhone(String number) throws IllegalArgumentException {
      if (null == number) throw new IllegalArgumentException("Phone number cannot be null");
      for (int counter = 0; counter < personCount; counter++) {
         if (number.equals(persons[counter].getPhoneNumber())) {
            return persons[counter];
         }
      }
      return null;
   }

   @Override
   public void printStatus() {
      System.out.println("Slow phonebook has " + personCount + " persons");
   }

   @Override
   public Person[] getPersons() {
      return persons;
   }

}
