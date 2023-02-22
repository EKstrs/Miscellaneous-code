package oy.tol.tra;

import java.lang.reflect.Array;

/**
 * An implementation of the StackInterface.
 * <p>
 * TODO: Students, implement this so that the tests pass.
 */
public class StackImplementation<E> implements StackInterface<E> {

   private E [] itemArray;
   private int capacity;
   private int currentIndex = -1;

   @Override
   public void init(Class<E> elementClass, int size) throws StackAllocationException {
      if (size < 2) {
         throw new StackAllocationException("Stack size should be greater than 1");
      }
      try {
         capacity = size;
         currentIndex = -1;
         itemArray = (E []) Array.newInstance(elementClass, capacity);   
      } catch (Exception e) {
         throw new StackAllocationException(e.getMessage());
      }
   }

   @Override
   public int capacity() {
      return capacity;
   }

   @Override
   public void push(E element) throws StackAllocationException, NullPointerException {
      if(element == null){
        throw new NullPointerException();
      }
      if(currentIndex == capacity -1){
         Class c = itemArray[0].getClass();
         int uusicapacity = capacity * 2;
         E[] uusiItem = (E []) Array.newInstance(c, uusicapacity);
         for(int i = 0; i < itemArray.length; i++){
            uusiItem[i] = itemArray[i];
         }
         capacity = uusicapacity;
         itemArray = uusiItem;
      
      }
      currentIndex = currentIndex + 1;
      itemArray[currentIndex] = element;
   }

   @Override
   public E pop() throws StackIsEmptyException {
      if(currentIndex == -1){
         throw new StackIsEmptyException("stack is empty");
      }
      currentIndex = currentIndex -1;
      return itemArray[currentIndex + 1];
      }

   @Override
   public E peek() throws StackIsEmptyException {
      if(currentIndex == -1){
         throw new StackIsEmptyException("Empty");
      }
      return itemArray[currentIndex];
   }

   @Override
   public int count() {
      return currentIndex + 1;
   }

   @Override
   public void reset() {
      for(int i = 0; i < capacity; i++){
         itemArray[i] = null;
      }
      currentIndex = -1;
   }

   @Override
   public boolean empty() {
      if (currentIndex  == -1)
         return true;
      else{
         return false;
      }
   }

}
