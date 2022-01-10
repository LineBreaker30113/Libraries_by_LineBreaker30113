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

	// actually just a mutext and dynamic memory manupilation wrapper for a type called contentT
	template<typename contentT = int, typename refCntrT = std::uint8_t> struct tbridge {

		struct zzm_bridgeT { contentT content; refCntrT refCntr = 1; std::mutex mutex; };
		zzm_bridgeT* zzm; contentT& onstart(); constexpr void on_end(); const contentT& readnow();

		tbridge(const tbridge& other); tbridge(tbridge&& other);
		tbridge(); ~tbridge(); tbridge(const contentT& val); tbridge(contentT&& val);
		constexpr void operator=(const tbridge& other); constexpr void operator=(tbridge&& other);
		constexpr void zzzm_onDelete(); constexpr void zzzm_doCopy(const tbridge& other);
	};

	template<typename cT, typename rCT> inline cT& tbridge<cT, rCT>::onstart() { zzm->mutex.lock(); return zzm->content; }
	template<typename cT, typename rCT> inline constexpr void tbridge<cT, rCT>::on_end() { zzm->mutex.unlock(); }
	template<typename cT, typename rCT> inline const cT& tbridge<cT, rCT>::readnow() { return zzm->content; }

	template<typename cT, typename rCT> inline tbridge<cT, rCT>::tbridge() : zzm(new zzm_bridgeT) {}
	template<typename cT, typename rCT> inline tbridge<cT, rCT>::~tbridge() { zzzm_onDelete(); }
	template<typename cT, typename rCT> inline tbridge<cT, rCT>::tbridge(tbridge&& other) : zzm(std::move(other.zzm)) {}
	template<typename cT, typename rCT> inline tbridge<cT, rCT>::tbridge(const tbridge& other) { zzzm_doCopy(other); }

	template<typename cT, typename rCT>	inline tbridge<cT, rCT>::tbridge(const cT& val) : zzm(new zzm_bridgeT)
	{ zzm->mutex.lock(); zzm->content = val; zzm->mutex.unlock(); }
	template<typename cT, typename rCT> inline tbridge<cT, rCT>::tbridge(cT&& val) : zzm(new zzm_bridgeT)
	{ zzm->mutex.lock(); zzm->content = std::move(val); zzm->mutex.unlock(); }

	template<typename cT, typename rCT>	inline constexpr void tbridge<cT, rCT>::operator=(const tbridge& other)
	{ zzzm_onDelete(); zzzm_doCopy(other); }
	template<typename cT, typename rCT>	inline constexpr void tbridge<cT, rCT>::operator=(tbridge&& other)
	{ zzzm_onDelete(); }


	template<typename cT, typename rCT>	inline constexpr void tbridge<cT, rCT>::zzzm_onDelete()
	{ zzm->mutex.lock(); zzm->refCntr--; if (!zzm->refCntr) { zzm->mutex.unlock(); delete zzm; return; } zzm->mutex.unlock(); }
	template<typename cT, typename rCT>	inline constexpr void tbridge<cT, rCT>::zzzm_doCopy(const tbridge& other)
	{ other.zzm->mutex.lock(); zzm = other.zzm; zzm->refCntr++; zzm->mutex.unlock(); }
}
