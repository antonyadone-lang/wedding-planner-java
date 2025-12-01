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
