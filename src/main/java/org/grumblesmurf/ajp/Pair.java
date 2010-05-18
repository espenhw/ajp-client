package org.grumblesmurf.ajp;

class Pair<T, U> 
{
    final T a;
    final U b;

    Pair(T a, U b) {
        this.a = a;
        this.b = b;
    }

    static <K,V> Pair<K, V> make(K k, V v) {
        return new Pair<K, V>(k, v);
    }
}

