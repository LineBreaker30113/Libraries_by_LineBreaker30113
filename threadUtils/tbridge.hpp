#pragma once
#include <mutex>

namespace aetl {// Abdulhalim ESEN's Template Library
	/*example use:
	* ...
	* void ththread_func(tbridge<long long> the_bridge){
	* ...
	* long long& content = the_bridge.onstart();
	* ...// do stuff with the content
	* the_bridge.stop();
	* ...
	* }
	* ...
	* int main(const char** argv, int argc){
	* ...
	* tbridge<long long> bridge;
	* std::thread the_thread(the_thread_func, bridge);
	* ...
	* long long& content = bridge.onstart();
	* ...// do stuff with the content
	* bridge.stop();
	* ...
	* }
	*/
	template<typename contentT, typename refCntrT = std::uint8_t>// referenceCounterType
	struct tbridge {// actually just a mutext and dynamic memory manupilation wrapper for a type called contentT
		struct zzm_bridgeT { contentT content; refCntrT refCntr = 1; std::mutex mutex; };
		zzm_bridgeT* zzm; contentT& onstart(); constexpr void on_end();

		void zzm_onDelete(); operator zzm_bridgeT& (); tbridge(); tbridge(zzm_bridgeT other); ~tbridge();

	};

	template<typename cT, typename rCT> inline cT& tbridge<cT, rCT>::onstart() { zzm->mutex.lock(); return zzm->content; }
	template<typename cT, typename rCT>	inline constexpr void tbridge<cT, rCT>::on_end() { zzm->mutex.unlock(); }

	template<typename cT, typename rCT>	inline void tbridge<cT, rCT>::zzm_onDelete() { zzm->refCntr--; if (!zzm->refCntr) { delete zzm; } }
	template<typename cT, typename rCT>	inline tbridge<cT, rCT>::operator zzm_bridgeT& () { return zzm->content; }
	template<typename cT, typename rCT>	inline tbridge<cT, rCT>::tbridge() : zzm(new zzm_bridgeT) {}
	template<typename cT, typename rCT>	inline tbridge<cT, rCT>::tbridge(zzm_bridgeT other)
		:zzm(std::move(other)) { zzm->refCntr++; }
	template<typename contentT, typename refCntrT> inline tbridge<contentT, refCntrT>::~tbridge() { zzm_onDelete(); }
}
