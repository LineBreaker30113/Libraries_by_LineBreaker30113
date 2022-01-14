#pragma once
#include <mutex>

namespace ael {// Abdulhalim ESEN's Template Library

	/*example use:
	* ...
	* void ththread_func(tbridgeT<long long> the_bridge){
	* ...
	* long long& content = the_bridge.onstart();
	* ...// do stuff with the content
	* the_bridge.stop();
	* ...
	* }
	* ...
	* int main(const char** argv, int argc){
	* ...
	* tbridgeT<long long> bridge;
	* std::thread the_thread(the_thread_func, bridge);
	* ...
	* long long& content = bridge.onstart();
	* ...// do stuff with the content
	* bridge.stop();
	* ...
	* }
	*/

	// actually just a mutext and dynamic memory manupilation wrapper for a type called contentT
	template<typename contentT = int, typename refCntrT = std::uint8_t> struct tbridgeT {

		struct zzm_bridgeT { contentT content; refCntrT refCntr = 1; std::mutex mutex; };
		zzm_bridgeT* zzm; contentT& onstart(); constexpr void on_end(); const contentT& readnow();

		tbridgeT(const tbridgeT& other); tbridgeT(tbridgeT&& other);
		tbridgeT(); ~tbridgeT(); tbridgeT(const contentT& val); tbridgeT(contentT&& val);
		constexpr void operator=(const tbridgeT& other); constexpr void operator=(tbridgeT&& other);
		constexpr void zzzm_onDelete(); constexpr void zzzm_doCopy(const tbridgeT& other);
	};

	template<typename cT, typename rCT> inline cT& tbridgeT<cT, rCT>::onstart() { zzm->mutex.lock(); return zzm->content; }
	template<typename cT, typename rCT> inline constexpr void tbridgeT<cT, rCT>::on_end() { zzm->mutex.unlock(); }
	template<typename cT, typename rCT> inline const cT& tbridgeT<cT, rCT>::readnow() { return zzm->content; }

	template<typename cT, typename rCT> inline tbridgeT<cT, rCT>::tbridgeT() : zzm(new zzm_bridgeT) {}
	template<typename cT, typename rCT> inline tbridgeT<cT, rCT>::~tbridgeT() { zzzm_onDelete(); }
	template<typename cT, typename rCT> inline tbridgeT<cT, rCT>::tbridgeT(tbridgeT&& other) : zzm(std::move(other.zzm)) {}
	template<typename cT, typename rCT> inline tbridgeT<cT, rCT>::tbridgeT(const tbridgeT& other) { zzzm_doCopy(other); }

	template<typename cT, typename rCT>	inline tbridgeT<cT, rCT>::tbridgeT(const cT& val) : zzm(new zzm_bridgeT)
	{ zzm->mutex.lock(); zzm->content = val; zzm->mutex.unlock(); }
	template<typename cT, typename rCT> inline tbridgeT<cT, rCT>::tbridgeT(cT&& val) : zzm(new zzm_bridgeT)
	{ zzm->mutex.lock(); zzm->content = std::move(val); zzm->mutex.unlock(); }

	template<typename cT, typename rCT>	inline constexpr void tbridgeT<cT, rCT>::operator=(const tbridgeT& other)
	{ zzzm_onDelete(); zzzm_doCopy(other); }
	template<typename cT, typename rCT>	inline constexpr void tbridgeT<cT, rCT>::operator=(tbridgeT&& other)
	{ zzzm_onDelete(); }


	template<typename cT, typename rCT>	inline constexpr void tbridgeT<cT, rCT>::zzzm_onDelete()
	{ zzm->mutex.lock(); zzm->refCntr--; if (!zzm->refCntr) { zzm->mutex.unlock(); delete zzm; return; } zzm->mutex.unlock(); }
	template<typename cT, typename rCT>	inline constexpr void tbridgeT<cT, rCT>::zzzm_doCopy(const tbridgeT& other)
	{ other.zzm->mutex.lock(); zzm = other.zzm; zzm->refCntr++; zzm->mutex.unlock(); }
}
