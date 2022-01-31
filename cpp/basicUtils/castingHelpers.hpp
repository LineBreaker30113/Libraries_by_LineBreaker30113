#pragma once

namespace ael {// Abdulhalim ESEN's Template Library

	template<typename sT = int, typename cT = float> struct autoCastedT {// storde as a thing readed as enoughter thing
		sT s;// means both self and storage
		operator cT&& () const { return (float)s; }
		template <typename T> autoCastedT(const T& var) : s(var) {}	autoCastedT(sT&& var) : s(std::move(var)) {}
	};
	template<typename sT = int, typename cT = float> struct quickCastedT {
		sT s;// means both self and storage
		operator sT& () const { return s; } cT&& c() { return (cT)s; }// "c" means cast
		quickCastedT(sT&& var) : s(std::move(var)) {}
		template<typename T> quickCastedT(const T& var) : s(var) {}
	};
}
