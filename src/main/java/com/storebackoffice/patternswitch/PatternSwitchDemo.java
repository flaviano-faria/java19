package com.storebackoffice.patternswitch;

/**
 * Demonstrates <strong>pattern matching for {@code switch}</strong> (JEP 427, preview in JDK 19;
 * finalized in JDK 21): type patterns on {@link Payment}, guarded cases with {@code when}, and
 * compiler-checked exhaustiveness for a {@code sealed} hierarchy.
 */
public final class PatternSwitchDemo {

    private PatternSwitchDemo() {
    }

    public static void main(String[] args) {
        Payment[] samples = {
                new Cash(500),
                new Cash(50_000),
                new Card("VISA", 12_000),
                new Card("AMEX", 99_000),
                new BankTransfer("ACH-001", 250_000),
        };

        for (Payment p : samples) {
            System.out.println(describe(p) + " | fee=" + feeCents(p) + " routing=" + routingHint(p));
        }
    }

    /** Labels each payment kind; uses guarded {@code case} for amount tiers. */
    static String describe(Payment payment) {
        return switch (payment) {
            case Cash c when c.cents() >= 10_000 -> "Large cash (" + c.cents() + " cents)";
            case Cash c -> "Cash (" + c.cents() + " cents)";
            case Card card when card.cents() > 50_000 -> "High-value card " + card.network();
            case Card card -> "Card " + card.network() + " **** amount=" + card.cents();
            case BankTransfer t -> "Bank transfer ref=" + t.reference();
        };
    }

    /**
     * Exhaustive {@code switch} on a {@code sealed} interface: the compiler rejects missing
     * permitted subtypes (try removing a {@code case} to see the error).
     */
    static int feeCents(Payment payment) {
        return switch (payment) {
            case Cash c -> Math.min(99, c.cents() / 100);
            case Card c -> 199 + (c.cents() > 25_000 ? 50 : 0);
            case BankTransfer t -> t.cents() > 100_000 ? 500 : 250;
        };
    }

    static String routingHint(Payment payment) {
        return switch (payment) {
            case Cash c -> c.cents() < 1_000 ? "teller" : "vault";
            case Card c -> c.network().equalsIgnoreCase("AMEX") ? "amex-net" : "card-processor";
            case BankTransfer t -> "ach";
        };
    }
}

/** Sealed sum type for payment variants (permits only the records below). */
sealed interface Payment permits Cash, Card, BankTransfer {
}

record Cash(int cents) implements Payment {
}

record Card(String network, int cents) implements Payment {
}

record BankTransfer(String reference, int cents) implements Payment {
}
