## Wedding Planner - Java OOP Project

Portfolio project demonstrating Object-Oriented Programming concepts in java.

##  Features
-Inheritance: "Testimone" extends "Invitato".
-Polymorphism: Override of 'presentati()' method.
-Encapsulation: Private attributes with getters/setters.
-Collections: ArrayList for dynamic guest management.
-Composition: 'Tavolo' contains guests, 'WeddingManager' coordinates

## Classes
-'Invitato': Base guest class.
-'Testimone': Witness (extends Invitato).
-'Tavolo': Table with capacity management.
-'WeddingManager': Central cooridnator.

## Technologies
-Java
-OOP principles.

## Suppliers and Cost Management

### Abstract Class Pattern
The project implements an abstract class `ServizioMatrimonio` that defines:
- Common attributes: `nomeFornitore`, `contatto`
- Abstract method: `calcolaCosto()` - implemented differently by each supplier
- Concrete method: `scheda()` - displays supplier information

### Supplier Classes
1. **Fotografo** (Photographer) - Fixed package cost
2. **DJ** - Fixed package cost
3. **Catering** - Cost per person × number of guests
4. **Fiorista** (Florist) - Fixed cost + transportation fee

### Cost Calculation
The `WeddingManager` class:
- Stores all suppliers in an `ArrayList<ServizioMatrimonio>`
- Calculates total wedding cost using polymorphism
- Each supplier's `calcolaCosto()` method is called dynamically

## Interface and Polymorphism
- **Interface**: `Tracciabile` for tracking task completion
- **Enum**: `StatoLavoro` for detailed state management in Fiorista
- **Implementations**: Fiorista (with Enum) and Compiti (with boolean)
- **Polymorphism**: WeddingManager manages different objects through same interface

## Static and Final Keywords

### Static Members
- **Static counter**: `Invitato.contatoreInvitati` - Tracks total number of guests created across all instances
- **Static method**: `getTotaleInvitati()` - Returns the total guest count without requiring an instance
- **Static constants**: `ConfigurazioneMatrimonio` class with shared configuration values

### Final Members
- **Final instance variable**: `idInvitato` - Unique immutable ID assigned to each guest at creation
- **Final constants**: Configuration values like `CAPACITA_MASSIMA_SALA`, `ALIQUOTA_IVA`, `MIN_INVITATI`

### Configuration Constants
The `ConfigurazioneMatrimonio` class provides:
- `CAPACITA_MASSIMA_SALA` (120) - Maximum venue capacity
- `ALIQUOTA_IVA` (0.22) - VAT rate for cost calculations
- `MIN_PERSONE_PER_TAVOLO` (6) - Minimum guests per table
- `MAX_PERSONE_PER_TAVOLO` (14) - Maximum guests per table
- `MIN_INVITATI` (100) - Minimum total guests required

## Generics and Type Safety

### Custom Generic Classes
The project implements custom generic classes for type-safe data handling:

#### Coppia<K, V>
Generic class for storing key-value pairs of any type:
- **Type parameters**: `K` (key), `V` (value)
- **Use case**: Pairing related data (e.g., supplier name with cost)
- **Methods**: `getChiave()`, `getValore()`, `toString()`

#### RisultatoOperazione<T>
Generic wrapper class for operation results (similar to Optional/Result pattern):
- **Type parameter**: `T` (result data type)
- **Attributes**: `dato` (T), `successo` (boolean), `messaggioErrore` (String)
- **Constructor overloading**: Success constructor (T dato) and failure constructor (String error)
- **Use case**: Structured error handling without exceptions or null returns
- **Example**: `cercaInvitatoPerEmail()` returns `RisultatoOperazione<Invitato>`

### Wildcards and Type Flexibility

#### Upper Bound Wildcard (`? extends T`)
Demonstrates generic type invariance and wildcard usage:
- **Method**: `stampaNomi(List<? extends Invitato> lista)`
- **Accepts**: `List<Invitato>`, `List<Testimone>`, or any subclass list
- **Key concept**: `List<Testimone>` is NOT a subtype of `List<Invitato>` (generic invariance)
- **Solution**: Wildcard `? extends Invitato` enables polymorphic list handling
- **PECS rule**: Producer Extends - use when reading from collection

### Benefits
- **Type safety**: Compile-time type checking prevents runtime errors
- **Code reusability**: Single generic class works with multiple types
- **Professional patterns**: RisultatoOperazione follows industry-standard error handling
- **Flexibility**: Wildcards enable working with inheritance hierarchies in collections

## Collections and Iterators

### Iterator Pattern for Safe Removal
Demonstrates safe collection modification during iteration using the Iterator pattern:

#### rimuoviInvitatiSenzaRSVP(LocalDate dataLimite)
Method in WeddingManager that removes guests who:
- Did not respond (dataRisposta == null)
- Responded after the deadline (dataRisposta.isAfter(dataLimite))
- Responded but did not confirm (confermato == false)

**Key Implementation**:
- Uses `Iterator<Invitato>` instead of for-each loop
- Calls `iterator.remove()` for safe removal during iteration
- Avoids `ConcurrentModificationException`
- Returns count of removed guests

### Date Management with LocalDate
Integration of `java.time.LocalDate` for RSVP tracking:
- **dataRisposta** attribute in Invitato class
- Date comparison with `.isAfter()` method
- Null-safe date validation
- Utility method `haRisposto()` for readability

## Collections Framework II - HashSet, HashMap, Lambda, Predicate

### HashSet for Email Uniqueness
Prevents duplicate guest registrations using email-based uniqueness:
- **HashSet<String> emailRegistrate** - O(1) lookup for duplicate detection
- **equals() and hashCode()** - Implemented in Invitato class based on email
- **aggiungiInvitato()** - Validates email uniqueness before adding guest

### HashMap for Supplier Lookup
Fast supplier retrieval by ID:
- **HashMap<Integer, ServiziMatrimonio> mappaFornitori** - O(1) access by supplier ID
- **Auto-increment ID system** - Each supplier gets unique ID via static counter
- **cercaFornitorePerId(int id)** - Direct supplier lookup without iteration

### Lambda Expressions and Functional Interfaces

#### Custom Functional Interface
**FiltroInvitato** - Custom functional interface for guest filtering:
- Single abstract method: `boolean soddisfaCondizione(Invitato inv)`
- **filtraInvitati(FiltroInvitato filtro)** - Accepts lambda expressions
- **Use case**: `filtraInvitati(inv -> inv.isConfermato())` - Filter confirmed guests

#### Built-in Predicate<T>
**Predicate<Invitato>** - Java's built-in functional interface for conditional removal:
- **rimuoviSe(Predicate<Invitato> criterio)** - Removes guests matching condition
- **Use case**: `rimuoviSe(inv -> !inv.haRisposto())` - Remove non-responders
- **Method reference support**: Works with lambda and method references

### Benefits
- **Performance**: O(1) operations for email validation and supplier lookup
- **Type safety**: Compile-time checking with generics
- **Code conciseness**: Lambda expressions reduce boilerplate
- **Flexibility**: Both custom and built-in functional interfaces demonstrated

## Stream API (Functional Programming)

### Stream Pipeline Concept
Streams provide a functional approach to processing collections through a pipeline of operations:
- **Source** → Collection (e.g., `listaInvitati`)
- **Intermediate Operations** → Transform data (filter, map, sorted) - lazy evaluation
- **Terminal Operations** → Produce result (collect, count, sum) - trigger execution

### Implemented Methods

#### calcoloCostoServiziNonPagati()
Calculates total cost of unpaid services using Stream API:
- **filter()** → Select only unpaid services (`!isPagato()`)
- **mapToDouble()** → Transform each service to its cost
- **sum()** → Aggregate total cost
- **Use case**: Financial tracking of pending payments

#### getInvitatiPerTavolo(int numeroTavolo)
Retrieves guests assigned to a specific table:
- **filter()** → Exclude guests without table assignment (null check)
- **filter()** → Select guests at requested table number
- **collect(Collectors.toList())** → Gather results into new list
- **Use case**: Table seating arrangement management

### Benefits
- **Conciseness**: Replaces verbose for-loops with declarative code
- **Readability**: Clear intent through method names
- **Performance**: Lazy evaluation optimizes processing
- **Maintainability**: Functional style reduces bugs

---

## Exception Handling

### Exception Hierarchy
All exceptions inherit from `Throwable`:
- **Error** → System-level problems (OutOfMemoryError) - should not be caught
- **Exception** → Application-level problems - should be handled
  - **Checked Exceptions** → Compiler-enforced handling (IOException)
  - **Unchecked Exceptions** → Optional handling (RuntimeException subclasses)

### File I/O with Exception Handling

#### ImpostazioniMatrimonio Class
Configuration class for wedding settings:
- **Attributes**: `budgetMassimo`, `dataEvento`, `location`
- **Purpose**: Store and manage wedding configuration data
- **toString()**: Formatted output of settings

#### caricaImpostazioni(String nomeFile)
Loads wedding settings from file with comprehensive error handling:
- **BufferedReader** → Efficient file reading with buffering
- **FileReader** → Opens file for character-based reading
- **try-catch-finally** → Ensures resource cleanup
- **Multiple catch blocks** → Specific handling for FileNotFoundException and IOException
- **Default values** → Returns fallback configuration on error
- **throws IOException** → Delegates exception to caller

### Custom Exceptions

#### TavoloPienoException
Custom checked exception for table capacity management:
- **extends Exception** → Checked exception (compiler-enforced handling)
- **Constructor**: Accepts custom error message
- **Use case**: Thrown when attempting to assign guest to full table

#### Implementation Pattern
1. **Create exception class** → Extend Exception with message constructor
2. **Throw exception** → Use `throw new TavoloPienoException(message)` when condition met
3. **Declare in signature** → Add `throws TavoloPienoException` to method
4. **Handle with try-catch** → Catch specific exception type in caller

#### Modified Methods
- **assegnaTavolo()** → Validates table capacity before assignment
- **Tavolo.getCapacitaMassima()** → Returns maximum table capacity
- **Tavolo.getNumeroOspiti()** → Returns current guest count

### Exception Handling Keywords
- **try** → Block containing code that may throw exceptions
- **catch** → Block handling specific exception types
- **finally** → Block always executed (resource cleanup)
- **throw** → Explicitly throws an exception
- **throws** → Declares method may throw exception (delegates to caller)

### Benefits
- **Robustness**: Graceful handling of error conditions
- **Clarity**: Custom exceptions communicate domain-specific errors
- **Maintainability**: Centralized error handling logic
- **User experience**: Meaningful error messages instead of crashes

## File I/O and Persistence

### Try-with-Resources Pattern
Modern resource management that automatically closes resources:
- **Syntax**: `try (Resource r = new Resource()) { }`
- **Benefits**: Automatic resource cleanup, no need for finally block
- **Implements**: AutoCloseable interface

### CSV File Operations
Text-based data persistence for human-readable storage:

#### salvaInvitatiSuFile(String nomeFile)
Saves guest list to CSV file:
- **BufferedWriter** → Efficient character-based writing
- **Format**: `nome,cognome,email,confermato`
- **Use case**: Export guest list for external tools

#### caricaInvitatiDaFile(String nomeFile)
Loads guest list from CSV file:
- **BufferedReader** → Efficient line-by-line reading
- **String.split(",")** → Parse CSV format
- **Reconstruction**: Creates new Invitato objects from data

#### svuotaTutto()
Clears both listaInvitati and emailRegistrate:
- **Encapsulation**: Single method to clear related data structures
- **Use case**: Simulate program closure or reset state

### Benefits
- **Portability**: CSV files readable by Excel, Google Sheets, etc.
- **Debugging**: Human-readable format for inspection
- **Interoperability**: Easy data exchange with other systems

## Object Serialization

### Serializable Interface
Marker interface enabling object serialization:
- No methods to implement
- Signals JVM that class can be serialized
- Subclasses automatically inherit serialization capability

### Implemented Classes
- Invitato implements Serializable
- ServiziMatrimonio implements Serializable (all subclasses inherit)

### Binary File I/O

#### salvaDatiBinari(String nomeFile)
Saves complete WeddingManager state to binary file:
- ObjectOutputStream serializes Java objects
- Preserves: listaInvitati, listaTavoli, elencoFornitori
- Format: Binary (not human-readable)
- Use case: Complete application state backup

#### caricaDatiBinari(String nomeFile)
Loads complete WeddingManager state from binary file:
- ObjectInputStream deserializes Java objects
- Type casting required after readObject()
- Order matters: Read objects in same order as written

### Derived Data Structure Reconstruction
After deserialization, HashMap and HashSet must be rebuilt:

emailRegistrate (HashSet) reconstruction:
- Clear existing HashSet
- Loop through listaInvitati
- Add each email to HashSet

mappaFornitori (HashMap) reconstruction:
- Clear existing HashMap
- Loop through elencoFornitori
- Put each supplier with ID as key

### CSV vs Serialization Comparison

CSV:
- Format: Text (human-readable)
- Speed: Slower
- Size: Larger
- Portability: Cross-platform
- Use case: Simple data, interoperability

Serialization:
- Format: Binary (compact)
- Speed: Faster
- Size: Smaller
- Portability: Java-only
- Use case: Complete object graphs

### Benefits
- Complete state preservation: Entire object graph saved
- Type safety: Objects restored with correct types
- Efficiency: Binary format faster than text parsing
- Simplicity: No manual parsing or formatting required

## Code Quality and Documentation

### Javadoc Documentation
Professional API documentation using Javadoc comments with special tags:
- @param describes method parameters
- @return describes return value
- @throws describes exceptions thrown

Documented Methods:
- assegnaTavolo() - Table assignment with capacity validation
- aggiungiInvitato() - Guest addition with email uniqueness check
- cercaInvitatoPerEmail() - Guest search with generic result wrapper
- rimuoviInvitatiSenzaRSVP() - Iterator-based safe removal
- caricaImpostazioni() - File I/O with exception handling
- salvaDatiBinari() - Binary serialization
- caricaDatiBinari() - Binary deserialization with reconstruction

### Professional Error Handling

#### System.err vs System.out
Separate error output stream for better visibility:
- System.out: Normal program output (black text)
- System.err: Error messages (red text in IDE)
- Benefit: Errors immediately visible, can be redirected separately

Error messages include:
- Clear error description
- Detailed cause with e.getMessage()

### Import Management

#### Explicit Imports
Professional code style using explicit imports instead of wildcards:
- Wildcard: import java.util.* (imports everything)
- Explicit: import java.util.ArrayList; (imports only what needed)

Benefits:
- Clarity: See exactly which classes are used
- No conflicts: Avoid ambiguous class names
- Best practice: Google Java Style Guide recommendation

### Code Formatting
Professional code organization following Java conventions:
- Indentation: 4 spaces consistent throughout
- Spacing: Spaces around operators
- Braces: Opening brace on same line, closing on new line
- Line length: Maximum 120 characters
- Import order: Alphabetical within package groups

### Final Keyword
Immutability for collection references:
- Applied to: emailRegistrate, mappaFornitori, listaTracciabili
- Meaning: Reference cannot be reassigned (content can change)
- Benefit: Prevents accidental reassignment, clearer intent
- Use case: Collections initialized once in constructor

## Creational Design Patterns

The project implements three core creational design patterns to manage object instantiation in a flexible, scalable, and maintainable way.

### Singleton Pattern
- **Class**: `WeddingManager`
- **Purpose**: Ensures that only one instance of `WeddingManager` exists throughout the application, providing a single global point of access to coordinate the event.
- **Implementation**: Achieved through a `private` constructor and a `public static synchronized getInstance()` method that handles lazy, thread-safe instantiation.

### Builder Pattern
- **Class**: `Invitato`
- **Purpose**: Simplifies the creation of `Invitato` objects, which have several optional attributes (`dietaSpeciale`, `allergie`, `numeroTelefono`). It provides a readable, fluent API.
- **Implementation**: A static inner `Builder` class within `Invitato` with method chaining (`return this`).

### Factory Method Pattern
- **Class**: `FornitoreFactory`
- **Purpose**: Decouples the client code from the concrete implementation of suppliers (`ServiziMatrimonio` subclasses). It centralizes the logic for creating different types of suppliers.
- **Implementation**: A factory class with a static method `creareFornitore()`. It takes a `TipoFornitore` enum to decide which object to instantiate (`Fotografo`, `Dj`, `Catering`, `Fiorista`) and uses `varargs` (`double...`) to handle varying constructor parameters.

## Latest Updates

### Strategy Pattern (Payment Management)
* **Description:** Implemented the *Strategy Pattern* to handle various payment methods (e.g., Bank Transfer, Credit Card) dynamically.
* **Benefits:**
    * **Decoupling:** Separated the payment calculation logic from the core application classes.
    * **Extensibility:** New payment methods can be added easily without modifying existing code (adhering to the Open/Closed Principle).

### Multithreading & Safe AutoSave System
* **Description:** Introduced an asynchronous **AutoSave** feature that runs in a background thread while the user interacts with the main menu.
* **Technical Details & Safety:**
    * **Runnable Interface:** Created the `AutoSaveTask` class to define the background execution logic.
    * **Thread Safety:** Implemented the `volatile` keyword to ensure immediate visibility of the termination signal across different threads.
    * **Graceful Shutdown:** Developed a clean exit strategy using `interrupt()` to wake the thread from its sleep cycle and `join()` to ensure the background process completes before the main application terminates.
    * **Timing:** The background save cycle is synchronized to trigger every 10 seconds using `Thread.sleep()`.

## Behavioral Design Patterns

### Strategy Pattern
The project implements the **Strategy Pattern** to handle interchangeable algorithms at runtime, specifically for sorting operations.

- **Interface**: `OrdinamentoStrategy`
- **Purpose**: Defines a common contract for sorting algorithms, allowing the `WeddingManager` to sort guests without knowing the specific implementation details.
- **Implementations**:
    - `OrdinamentoPerCognome`: Sorts guests alphabetically by surname.
    - `OrdinamentoPerStato`: Sorts guests based on their RSVP confirmation status.
- **Benefit**: Eliminates complex conditional logic (if/else or switch) inside the manager and allows adding new sorting criteria without modifying existing code (Open/Closed Principle).

## Robustness and Custom Exceptions

The project demonstrates a professional approach to error handling by defining domain-specific exceptions. This moves away from generic Java exceptions towards a "speaking code" architecture where errors describe business problems.

### Custom Exception Hierarchy

#### 1. Business Logic Exceptions (Checked)
**`BudgetSuperatoException`**
- **Type**: Checked Exception (`extends Exception`)
- **Trigger**: Thrown when adding a supplier would exceed the defined wedding budget.
- **Handling**: Forces the caller to decide whether to increase the budget or reject the supplier.

#### 2. Data Integrity Exceptions (Checked)
**`DatiInvitatoException`**
- **Type**: Checked Exception (`extends Exception`)
- **Trigger**: Thrown during CSV parsing when a line is malformed or missing required fields.
- **Handling**: Used inside the parsing loop to skip specific corrupted lines while allowing the rest of the file to load successfully.

#### 3. Runtime State Exceptions (Unchecked)
**`InvitatoNonTrovatoException`**
- **Type**: Unchecked Exception (`extends RuntimeException`)
- **Trigger**: Thrown when searching/confirming a guest by email fails.
- **Handling**: Caught at the UI level (Main) to show a friendly error message to the user.

### Implementation Example
How the `WeddingManager` enforces the budget constraint.

## Technical Highlights & Refactoring

### Data Structure Optimization (The "Big Switch")
To improve performance and demonstrate advanced Java collections knowledge, the `WeddingManager` has been refactored to use a hybrid data structure approach:

- **HashMap Implementation**: Transformed guest lookup from $O(n)$ (linear search) to **$O(1)$ (constant time)** using a `HashMap<String, Invitato>`. This ensures instant retrieval regardless of the guest list size.
- **Synchronized Collections**: For educational demonstration, the system maintains synchronization between three distinct structures:
  - `ArrayList`: Preserves insertion order.
  - `HashSet`: Ensures unique email constraints.
  - `HashMap`: Enables high-performance access by key (email).
- **Key Methods**:
  - `aggiungiInvitato()`: Updates all three collections atomically.
  - `cercaInvitatoPerEmail()`: Leverages HashMap for instant results.
  - `rimuoviInvitatiSenzaRSVP()`: Uses iterators to safely remove elements from all structures simultaneously.
    
  - ### Architecture & Design Patterns (Refactoring)

To ensure scalability and maintainability, the project has undergone significant architectural improvements applying **SOLID principles**:

- **Single Responsibility Principle (SRP)**:
  - Extracted all file I/O operations from `WeddingManager` into a dedicated `GestoreFile` utility class.
  - `WeddingManager` now focuses solely on business logic (guest management, budget, tables), delegating persistence tasks.

- **Exception Hierarchy**:
  - Introduced a centralized error handling strategy with a base class `WeddingException`.
  - All domain-specific exceptions (e.g., `TavoloPienoException`, `InvitatoNonTrovatoException`) now extend `WeddingException`, allowing for unified error catching and handling in the main application flow.

- **Code Organization**:
  - Restructured `WeddingManager` using a standard layout (Attributes -> Singleton -> Business Logic -> Persistence -> Utils) to improve readability and navigation.
---
*Note: This project is part of my professional growth in Java Development. It demonstrates my ability to handle design patterns and concurrent programming.*
