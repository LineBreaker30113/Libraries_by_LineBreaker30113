#pragma once

namespace ael {// Abdulhalim ESEN's Template Library
	
	template<typename T> struct heapAllcT {// simply makes someting allocated on the heap
		T* const p;// the variable pointer
		operator T& () const { return *p; }	heapAllcT() : p(new T) {} ~heapAllcT() { delete p; }

		template<typename T0> const T& operator+ (const T0& r) const { return (*p) + r; }
		template<typename T0> const T& operator- (const T0& r) const { return (*p) - r; }
		template<typename T0> const T& operator* (const T0& r) const { return (*p) * r; }
		template<typename T0> const T& operator/ (const T0& r) const { return (*p) / r; }
		template<typename T0> const T& operator% (const T0& r) const { return (*p) % r; }
		template<typename T0> const T& operator& (const T0& r) const { return (*p) & r; }
		template<typename T0> const T& operator| (const T0& r) const { return (*p) | r; }
		template<typename T0> const T& operator^ (const T0& r) const { return (*p) ^ r; }
		template<typename T0> const T& operator<<(const T0& r) const { return (*p) << r; }
		template<typename T0> const T& operator>>(const T0& r) const { return (*p) >> r; }
		template<typename T0> bool operator&&(const T0& r) const { return ((*p) && r); }
		template<typename T0> bool operator||(const T0& r) const { return ((*p) || r); }

		template<typename T0> const T& operator = (const T0& r) const { return (*p = r); }
		template<typename T0> const T& operator+= (const T0& r) const { return (*p += r); }
		template<typename T0> const T& operator-= (const T0& r) const { return (*p -= r); }
		template<typename T0> const T& operator*= (const T0& r) const { return (*p *= r); }
		template<typename T0> const T& operator/= (const T0& r) const { return (*p /= r); }
		template<typename T0> const T& operator%= (const T0& r) const { return (*p %= r); }
		template<typename T0> const T& operator&= (const T0& r) const { return (*p &= r); }
		template<typename T0> const T& operator|= (const T0& r) const { return (*p |= r); }
		template<typename T0> const T& operator^= (const T0& r) const { return (*p ^= r); }
		template<typename T0> const T& operator<<=(const T0& r) const { return (*p <<= r); }
		template<typename T0> const T& operator>>=(const T0& r) const { return (*p >>= r); }

		template<typename T0> bool operator==(const T0& r) const { return (*p) == r; }
		template<typename T0> bool operator>=(const T0& r) const { return (*p) >= r; }
		template<typename T0> bool operator<=(const T0& r) const { return (*p) <= r; }
		template<typename T0> bool operator> (const T0& r) const { return (*p) > r; }
		template<typename T0> bool operator< (const T0& r) const { return (*p) < r; }

		template<typename T0> const T& operator-  () const { return (-(*p)); }
		template<typename T0> const T& operator~  () const { return (~(*p)); }
		template<typename T0> const T& operator!  () const { return (!(*p)); }
		template<typename T0> const T& operator++ () const { return (++(*p)); }
		template<typename T0> const T& operator-- () const { return (--(*p)); }

		template<typename T0> T& operator-> () const { return *p; }
	};
	template<typename T> const T& operator++ (const heapAllcT<T>& r) { return ((*r.p)++); }
	template<typename T> const T& operator-- (const heapAllcT<T>& r) { return ((*r.p)--); }
}
