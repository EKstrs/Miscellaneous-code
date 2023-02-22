package oy.tol.tra;

public class Person implements Comparable<Person> {
    static final int MAX_TABLE_SIZE = 10000000;
    private String phoneNumber;
    private String firstName;
    private String lastName;

    public Person(String phone) {
        phoneNumber = phone;
    }

    public Person(String phone, String firstName, String lastName) {
        phoneNumber = phone;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    /**
     * TODO: Implement the method below to return a hash value. It can be based on the phone number
     * of the person alone
     * 
     * If you implement hash based on other than phone number, do note the relationship between the
     * hashCode() and equals() that is assumed in Java.
     * @return Hash value of the person.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        for(int i = 0; i < getPhoneNumber().length(); i++){
            hash = ((hash*31) + getPhoneNumber().charAt(i));
        }
        return hash;
    }

    public boolean equals(Object other) {
        if (other instanceof Person) {
            return this.phoneNumber.equals(((Person)other).phoneNumber);
        }
        return false;
    }

    /**
     * Compares two persons, this and the other one.
     * <p>
     * In a phonebook, persons are identified by the phone number.
     * So if a person is the same or another, depends on if they have
     * the same phone number.
     * <p>
     * Return <0 if the phone number (as string) is smaller than the others.
     * Return 0 if the phone numbers are identical.
     * Return >0 if the other persons phone number is larger (as string).
     * Note: String class also implements <code>compareTo()</code> you can use here.
     * @returns Returns 0 if persons are the same otherwise depending on the number, <0 or >0.
     */
    @Override
    public int compareTo(Person other) {
        return phoneNumber.compareTo(other.phoneNumber);
    }
}