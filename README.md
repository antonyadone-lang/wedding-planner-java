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
