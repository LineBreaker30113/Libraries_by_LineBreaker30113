#pragma once

namespace ael {// Abdulhalim ESEN's Template Library

	template<typename T> struct reference {// a pointer acts as an reference
		T* p;// the variable pointer
		operator T& () const { return *p; }
		reference() : p(nullptr) {} reference(T& var) : p(&var) {} T& operator() (T& var) { p = &var; return *p; }

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

		template<typename T0> const T& operator*  () const { return (*(*p)); }
		template<typename T0> const T& operator-  () const { return (-(*p)); }
		template<typename T0> const T& operator~  () const { return (~(*p)); }
		template<typename T0> const T& operator!  () const { return (!(*p)); }
		template<typename T0> const T& operator++ () const { return (++(*p)); }
		template<typename T0> const T& operator-- () const { return (--(*p)); }

		template<typename T0> const T& operator[](const T0& i) const { return (*p)[i]; }
		template<typename T0> T& operator-> () const { return *p; }

	};
	template<typename T> const T& operator++ (reference<T> ref) { return ((*ref.p)++); }
	template<typename T> const T& operator-- (reference<T> ref) { return ((*ref.p)--); }
}
