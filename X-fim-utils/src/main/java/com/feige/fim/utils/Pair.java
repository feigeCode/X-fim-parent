package com.feige.fim.utils;


public class Pair<K,V> {
	
	private K k;
	private V v;
	
	public Pair(K k, V v) {
		this.k = k;
		this.v = v;
	}
	public static <K,V> Pair<K,V> of(K k, V v){
		return new Pair<>(k,v);
	}
	
	public K getK() {
		return k;
	}
	public V getV() {
		return v;
	}
	public void setK(K k) {
		this.k = k;
	}
	public void setV(V v) {
		this.v = v;
	}

	@Override
	public String toString() {
		return "Pair{" +
				"k=" + k +
				", v=" + v +
				'}';
	}
}
