#pragma once

namespace ael {// Abdulhalim ESEN's Template Library

	template<typename T> struct referenceT {// a pointer acts as an referenceT
		T* p;// the variable pointer
		operator T& () const { return *p; }
		referenceT() : p(nullptr) {} referenceT(T& var) : p(&var) {} T& operator() (T& var) { p = &var; return *p; }

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
		const T*& operator->() const { return p; }

	};
	template<typename T> const T& operator++ (const referenceT<T>& ref) { return ((*ref.p)++); }
	template<typename T> const T& operator-- (const referenceT<T>& ref) { return ((*ref.p)--); }
}
