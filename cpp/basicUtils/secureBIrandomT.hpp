#pragma once
#include<chrono>
#include<cstdlib>

namespace ael {// Abdulhalim ESEN's Library
    template<typename T=float, typename prcsnT = std::chrono::seconds>
    struct secureBIrandomT{// simple object that handles that sometink happens with limited amouth of time within a time
        // for using: set ("dur","cnt") atributes of minC and maxC then define changeSpace and change to evaluating change
        using dT = std::chrono::duration<T, prcsnT>; using cT = std::chrono::high_resolution_clock;
        using tpT = std::chrono::time_point<cT, dT>;
        template<class durT, class reqT> struct pair { durT dur, zm_durcnt; reqT cnt, zm_cntr = 0; };
        pair<dT, int> minC, maxC;// will try to make it that the"reqval" is returned within defined space in each time periot
        dT zm_callDelay = 1.f; tpT zm_lastCall; int changeSpace = 100, change = 50, & cS = changeSpace, & c = change;
        bool&& getRanV(); constexpr void zm_oncall(); bool&& zm_isRndmable(); bool&& zm_getEvaluated();
        template<typename T1, typename T2> constexpr void initSameAS(const secureBIrandomT<T1,T2>& other);
    };
    template<typename T, typename pT> inline bool&& secureBIrandomT<T, pT>::getRanV() {
        zm_oncall(); const bool& val = zm_getEvaluated(); minC.zm_cntr += val; maxC.zm_cntr += val; return std::move(val);
    }
    template<typename T, typename pT> inline bool&& secureBIrandomT<T, pT>::zm_getEvaluated() {
        bool isR = zm_isRndmable(); return std::move(isR * (c => std::rand() % cS));
    }
    template<typename T, typename pT> inline constexpr void secureBIrandomT<T, pT>::zm_oncall() {
        tpT dif = cT::now() - zm_lastCall; zm_lastCall += dif;
        { int callCnt = minC.zm_cntr + maxC.zm_cntr + 1; zm_callDelay = (callCnt * zm_callDelay + dif) / (pT)(callCnt + 1); }

        minC.zm_durcnt += dif; maxC.zm_durcnt += dif;
        bool minRst = (minC.dur > minC.zm_durcnt); bool maxRst = (maxC.dur > maxC.zm_durcnt);
        minC.zm_durcnt -= minRst * minC.dur; minC.zm_cntr -= minRst * minC.zm_cntr;
        maxC.zm_durcnt -= maxRst * minC.dur; maxC.zm_cntr -= maxRst * maxC.zm_cntr;
        std::srand(cT::now().time_since_epoch().count());
    }
    template<typename T, typename pT> inline bool&& secureBIrandomT<T, pT>::zm_isRndmable() {
        return !(maxC.cnt == maxC.zm_cntr) && ((minC.cnt < minC.zm_cntr)||(zm_callDelay * 2 * (minC.cnt - minC.zm_cntr) > minC.dur));
    }
    template<typename T, typename pT> template<typename T1, typename T2>
    inline constexpr void secureBIrandomT<T, pT>::initSameAS(const secureBIrandomT<T1, T2>& other)
    { c = other.c; cS = other.cS; maxC.dur = other.maxC.dur; maxC.cnt = other.maxC.cnt; minC.dur = other.minC.dur; minC.cnt = other.minC.cnt; }
}
