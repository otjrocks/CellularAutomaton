# Collections API Lab Discussion
### Owen Jennings (otj4), Justin Aronwald (jga18), Troy Ludwig (tdl21)
### Cell Society Group #4



## In your experience using these collections, are they hard or easy to use?

Collections are relatively easy to learn, because you do not need to know the specific implementation of each collection type.
Additionally, since Collections are designed as an interface, once you learn how to interact with one specific implementation of a collection, you can easily learn how to use others.
Once you learn how to implement say a HashSet, you can easily implement a TreeSet, because both are implementing the same interact and have similar method calls. 
Even though the data structure used to store the data is different, you interaction with the data is predefined and clear through the API.

## In your experience using these collections, do you feel mistakes are easy to avoid?

In general, the way that the collections are defined so that it is hard to mistakes. 
The principals of the design are high performance with low effort from the programmer.
There are ways to misuse collections, but most common uses prevent mistakes.
One mistake that we have run into multiple times is a concurrent modification issue, where we try to access an element that has been removed from a collection, which can be unclear to deal with this API.

## What methods are common to all collections (except Maps)?

From collections interface implementation: 

    int size();

    boolean isEmpty();

 
    boolean contains(Object o);

    Iterator<E> iterator();

   
    Object[] toArray();

    
    <T> T[] toArray(T[] a);

    default <T> T[] toArray(IntFunction<T[]> generator) {
        return toArray(generator.apply(0));
    }


    boolean add(E e);

    boolean remove(Object o);

    boolean containsAll(Collection<?> c);

    boolean addAll(Collection<? extends E> c);

   
    boolean removeAll(Collection<?> c);

    default boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final Iterator<E> each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

   
    boolean retainAll(Collection<?> c);

   
    void clear();

    boolean equals(Object o);

  
    int hashCode();



## What methods are common to all Deques?

peak, pull, remove


## What is the purpose of each interface implemented by LinkedList?

Serializable:
- Convert something to a stream of bytes for transmition and saving

Cloneable:
- Allows for field to field copies of an instance

Iterable: 
- Allows for the creation of an iterator 

Collection:
- Allows LinkedList to implement the required methods for a collection

Deque:
- A LinkedList that allows you to add and remove from both ends

List:
- Allows you to use LinkedList as a List object

Queue:
- Provides additional insertion, extraction, and inspection methods

SequencedCollection:
- Maintains an ordered sequence of elements

## How many different implementations are there for a Set?

There are a total of 9.

## What is the purpose of each superclass of PriorityQueue?

AbstractQueue is used as a skeletal/minimal implementation of some required queue operations of a PriorityQueue
AbstractCollection is an implementation of a collection interface to minimize the effort needed to implement the Collection interface.
Object is root of the class hierarchy, which is automatically the superclass of any java class.

## What is the purpose of the Collections utility class?

To implement the functionality of a storage system to the classes that require it, without having to repeat code over many methods.


## API Characteristics applied to Collections API

* Easy to learn: We agree that the Collections API is easy to learn as most of its methods are easy to understand from their names

* Encourages extension: We agree, since the collection interface can be used to store really anything that is a collection ("sometimes called a container") of objects or items.

* Leads to readable code: Yes, however java itself is a very verbose language, causing the code to be less readable to someone who has little experience with the syntax.
* Languages like Python make it easy to read and understand how collections work.

* Hard to misuse: Yes, we agree that the API is hard to misuse. Most of the methods do what they are named. However, we do think that it could be redesigned to help the user better handle concurrent modification exceptions.
 
 