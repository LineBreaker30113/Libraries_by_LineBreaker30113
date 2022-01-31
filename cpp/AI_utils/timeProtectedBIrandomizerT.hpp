#pragma once
#include<chrono>
#include<cstdlib>

namespace ael {// Abdulhalim ESEN's Library
    using namespace std::chrono_literals;
    template<typename T=float, typename prcsnT = std::chrono::seconds>
    class timeProtectedBIrandomizerT{// simple object that handles that sometink happens with limited amouth of time within a time
        // for using: set ("dur","cnt") atributes of zm_minC and zm_maxC then define changeSpace and change to evaluating change
        using dT = std::chrono::duration<T, prcsnT>; using mc = std::chrono::high_resolution_clock;
        using tpT = mc::time_point; using numberT = unsigned long;

        template<class durT, class reqT> struct zm_pair { durT dur, zm_durcnt; reqT cnt, zm_cntr = 0; };
    public:
        zm_pair<dT, numberT> zm_minC, zm_maxC;// will try to make it that the"reqval" is returned within defined space in each time periot
        dT zm_callDelay = std::chrono::duration_cast<dT>(1s); tpT zm_lastCall; numberT changeSpace = 100, change = 50;
        bool&& getRandomValue(); constexpr void zm_oncall(); bool&& zm_isRndmable(); bool&& zm_getEvaluated();
        template<typename T1, typename T2> constexpr void initSameAS(const timeProtectedBIrandomizerT<T1,T2>& other);
        constexpr void setminReq(const dT& timesection, const numberT& request);//makes it will happen atleast "request" times within every "timesection"s
        constexpr void setmaxReq(const dT& timesection, const numberT& request);//makes it will happen only much as "request" times within every "timesection"s
        constexpr void setChange(const numberT& changeSpace, const numberT& change);//"changeSpace"is the possible values, "change" is thi part that returns true
    private: numberT &cS = changeSpace, & c = change;
    public:
        dT& minRsTS = zm_minC.dur, & maxRsTS = zm_maxC.dur;// time span for requested event
        numberT& maxR = zm_maxC.cnt, & minR = zm_minC.cnt;// request count
    };
    template<typename T, typename pT> inline bool&& timeProtectedBIrandomizerT<T, pT>::getRandomValue() {
        zm_oncall(); bool&& val = zm_getEvaluated(); zm_minC.zm_cntr += val; zm_maxC.zm_cntr += val; return std::move(val);
    }
    template<typename T, typename pT> inline bool&& timeProtectedBIrandomizerT<T, pT>::zm_getEvaluated() {
        bool isR = zm_isRndmable(); return std::move(isR * (c >= std::rand() % cS));
    }
    template<typename T, typename pT> inline constexpr void timeProtectedBIrandomizerT<T, pT>::zm_oncall() {
        auto dif = mc::now() - zm_lastCall; zm_lastCall += dif;
        { int callCnt = zm_minC.zm_cntr + zm_maxC.zm_cntr + 1; zm_callDelay = (callCnt * zm_callDelay + dif) / (pT)(callCnt + 1); }

        zm_minC.zm_durcnt += std::chrono::duration_cast<dT>(dif); zm_maxC.zm_durcnt += std::chrono::duration_cast<dT>(dif);
        bool minRst = (zm_minC.dur > zm_minC.zm_durcnt); bool maxRst = (zm_maxC.dur > zm_maxC.zm_durcnt);
        zm_minC.zm_durcnt -= minRst * zm_minC.dur; zm_minC.zm_cntr -= minRst * zm_minC.zm_cntr;
        zm_maxC.zm_durcnt -= maxRst * zm_minC.dur; zm_maxC.zm_cntr -= maxRst * zm_maxC.zm_cntr;
    }
    template<typename T, typename pT> inline bool&& timeProtectedBIrandomizerT<T, pT>::zm_isRndmable() {
        return !(zm_maxC.cnt == zm_maxC.zm_cntr) && ((zm_minC.cnt < zm_minC.zm_cntr) ||
            (zm_callDelay * ((T)(changeSpace-change)/(T)changeSpace + 1) * (zm_minC.cnt - zm_minC.zm_cntr) > zm_minC.dur));
    }
    template<typename T, typename pT> template<typename T1, typename T2>
    inline constexpr void timeProtectedBIrandomizerT<T, pT>::initSameAS(const timeProtectedBIrandomizerT<T1, T2>& other)
    { c = other.c; cS = other.cS; zm_maxC.dur = other.zm_maxC.dur; zm_maxC.cnt = other.zm_maxC.cnt;
    zm_minC.dur = other.zm_minC.dur; zm_minC.cnt = other.zm_minC.cnt; }

    template<typename T, typename pT> inline constexpr void timeProtectedBIrandomizerT<T, pT>::setminReq(const dT& timesection, const numberT& request)
    { zm_minC.dur = timesection; zm_minC.cnt = request; }

    template<typename T, typename pT> inline constexpr void timeProtectedBIrandomizerT<T, pT>::setmaxReq(const dT& timesection, const numberT& request)
    { zm_maxC.dur = timesection; zm_maxC.cnt = request; }

    template<typename T, typename pT> inline constexpr void timeProtectedBIrandomizerT<T, pT>::setChange(const numberT& changeSpace, const numberT& change)
    { changeSpace = changeSpace; change = change; std::srand(mc::now().time_since_epoch().count()); }

}
