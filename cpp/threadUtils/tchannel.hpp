// currently doesn't work just here to show you the idea
#pragma once
#include <chrono>
#include <thread>
#include <mutex>
#include <bitset>

namespace ael {// Abdulhalim ESEN's Template Library
	// simple struct for passing values across threads
	template<typename cT, std::uint32_t cD = 100, typename rCT = std::uint8_t> struct tchannel {

		struct zzm_pipeT { cT content; rCT refCntr = 1; std::mutex mutex; std::bitset<8> flags; };

		zzm_pipeT* zzm; constexpr void onstart(); std::bitset<8>& flags(); cT& content(); constexpr void on_end();
		const cT& readnow();
		//wait: waits a single turn, waitfor: waits until a flag is on
		constexpr void wait(); constexpr void zm_waitfor(); template<typename indexT> constexpr void waitfor(const indexT& id);
		//waits until a flag is on and the turns it of
		constexpr void zzm_expect(); template<typename indexT> constexpr void zm_expect(const indexT& id);

		tchannel(); tchannel(zzm_pipeT* other); tchannel(zzm_pipeT*&& other); ~tchannel();
		constexpr void operator=(const zzm_pipeT*& other); constexpr void operator=(zzm_pipeT*&&);
		constexpr void zzzm_onDelete(); constexpr void zzzm_doCopy(zzm_pipeT* other); constexpr void zzzm_doMove(zzm_pipeT*&& other);

		template<typename outT> constexpr void operator>>(outT& out); template<typename inpT> constexpr void operator<<(const inpT& inp);

	};


	template<typename cT, std::uint32_t cD, typename rCT>
	inline constexpr void tchannel<cT, cD, rCT>::onstart()
	{
	}

	template<typename cT, std::uint32_t cD, typename rCT>
	inline std::bitset<8>& tchannel<cT, cD, rCT>::flags()
	{
		// TODO: insert return statement here
	}

	template<typename cT, std::uint32_t cD, typename rCT>
	inline cT& tchannel<cT, cD, rCT>::content()
	{
		// TODO: insert return statement here
	}

	template<typename cT, std::uint32_t cD, typename rCT>
	inline constexpr void tchannel<cT, cD, rCT>::on_end()
	{
	}

	template<typename cT, std::uint32_t cD, typename rCT>
	inline const cT& tchannel<cT, cD, rCT>::readnow()
	{
		// TODO: insert return statement here
	}

	template<typename cT, std::uint32_t cD, typename rCT>
	inline constexpr void tchannel<cT, cD, rCT>::wait()
	{
	}

	template<typename cT, std::uint32_t cD, typename rCT>
	inline constexpr void tchannel<cT, cD, rCT>::zm_waitfor()
	{
	}

	template<typename cT, std::uint32_t cD, typename rCT>
	inline constexpr void tchannel<cT, cD, rCT>::zzm_expect()
	{
	}

	template<typename cT, std::uint32_t cD, typename rCT>
	inline tchannel<cT, cD, rCT>::tchannel()
	{
	}

	template<typename cT, std::uint32_t cD, typename rCT>
	inline tchannel<cT, cD, rCT>::tchannel(zzm_pipeT* other)
	{
	}

	template<typename cT, std::uint32_t cD, typename rCT>
	inline tchannel<cT, cD, rCT>::~tchannel()
	{
	}

	template<typename cT, std::uint32_t cD, typename rCT>
	inline constexpr void tchannel<cT, cD, rCT>::zzzm_onDelete()
	{
	}

	template<typename cT, std::uint32_t cD, typename rCT>
	inline constexpr void tchannel<cT, cD, rCT>::zzzm_doCopy(zzm_pipeT* other)
	{
	}

	template<typename cT, std::uint32_t cD, typename rCT> template<typename indexT>
	inline constexpr void tchannel<cT, cD, rCT>::waitfor(const indexT& id) { while (!zzm->flags[id]) { wait(); } }

	template<typename cT, std::uint32_t cD, typename rCT> template<typename indexT>
	inline constexpr void tchannel<cT, cD, rCT>::zm_expect(const indexT& id)
	{ waitfor(id); onstart(); zzm->flags[id] = false; on_end();	}

	template<typename cT, std::uint32_t cD, typename rCT> template<typename outT>
	inline constexpr void tchannel<cT, cD, rCT>::operator>>(outT& out) { out = zzm->content; }

	template<typename cT, std::uint32_t cD, typename rCT> template<typename inpT>
	inline constexpr void tchannel<cT, cD, rCT>::operator<<(const inpT& inp) { onstart(); zzm->content = inp; on_end(); }

}
