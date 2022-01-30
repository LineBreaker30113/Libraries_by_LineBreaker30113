#pragma once
#include<chrono>
#include<cstdlib>

namespace ael {// Abdulhalim ESEN's Library
    using namespace std::chrono_literals;
    template<typename T=float, typename prcsnT = std::chrono::seconds>
    class secureBIrandomT{// simple object that handles that sometink happens with limited amouth of time within a time
        // for using: set ("dur","cnt") atributes of zm_minC and zm_maxC then define zm_changeSpace and zm_change to evaluating zm_change
        using dT = std::chrono::duration<T, prcsnT>; using mc = std::chrono::high_resolution_clock;
        using tpT = mc::time_point;
    public:
        template<class durT, class reqT> struct zm_pair { durT dur, zm_durcnt; reqT cnt, zm_cntr = 0; };
        zm_pair<dT, int> zm_minC, zm_maxC;// will try to make it that the"reqval" is returned within defined space in each time periot
        dT zm_callDelay = std::chrono::duration_cast<dT>(1s); tpT zm_lastCall; int zm_changeSpace = 100, zm_change = 50;
        bool&& getRandomValue(); constexpr void zm_oncall(); bool&& zm_isRndmable(); bool&& zm_getEvaluated();
        template<typename T1, typename T2> constexpr void initSameAS(const secureBIrandomT<T1,T2>& other);
        constexpr void setminReq(const dT& timesection, int request);//makes it will happen atleast "request" times within every "timesection"s
        constexpr void setmaxReq(const dT& timesection, int request);//makes it will happen only much as "request" times within every "timesection"s
        constexpr void setChange(const int& changeSpace, int change);//"zm_changeSpace"is the possible values, "zm_change" is thi part that returns true
    private: int& cS = zm_changeSpace, & c = zm_change;
    };
    template<typename T, typename pT> inline bool&& secureBIrandomT<T, pT>::getRandomValue() {
        zm_oncall(); bool&& val = zm_getEvaluated(); zm_minC.zm_cntr += val; zm_maxC.zm_cntr += val; return std::move(val);
    }
    template<typename T, typename pT> inline bool&& secureBIrandomT<T, pT>::zm_getEvaluated() {
        bool isR = zm_isRndmable(); return std::move(isR * (c >= std::rand() % cS));
    }
    template<typename T, typename pT> inline constexpr void secureBIrandomT<T, pT>::zm_oncall() {
        auto dif = mc::now() - zm_lastCall; zm_lastCall += dif;
        { int callCnt = zm_minC.zm_cntr + zm_maxC.zm_cntr + 1; zm_callDelay = (callCnt * zm_callDelay + dif) / (pT)(callCnt + 1); }

        zm_minC.zm_durcnt += std::chrono::duration_cast<dT>(dif); zm_maxC.zm_durcnt += std::chrono::duration_cast<dT>(dif);
        bool minRst = (zm_minC.dur > zm_minC.zm_durcnt); bool maxRst = (zm_maxC.dur > zm_maxC.zm_durcnt);
        zm_minC.zm_durcnt -= minRst * zm_minC.dur; zm_minC.zm_cntr -= minRst * zm_minC.zm_cntr;
        zm_maxC.zm_durcnt -= maxRst * zm_minC.dur; zm_maxC.zm_cntr -= maxRst * zm_maxC.zm_cntr;
    }
    template<typename T, typename pT> inline bool&& secureBIrandomT<T, pT>::zm_isRndmable() {
        return !(zm_maxC.cnt == zm_maxC.zm_cntr) && ((zm_minC.cnt < zm_minC.zm_cntr) ||
            (zm_callDelay * ((T)(zm_changeSpace-zm_change)/(T)zm_changeSpace + 1) * (zm_minC.cnt - zm_minC.zm_cntr) > zm_minC.dur));
    }
    template<typename T, typename pT> template<typename T1, typename T2>
    inline constexpr void secureBIrandomT<T, pT>::initSameAS(const secureBIrandomT<T1, T2>& other)
    { c = other.c; cS = other.cS; zm_maxC.dur = other.zm_maxC.dur; zm_maxC.cnt = other.zm_maxC.cnt;
    zm_minC.dur = other.zm_minC.dur; zm_minC.cnt = other.zm_minC.cnt; }

    template<typename T, typename pT> inline constexpr void secureBIrandomT<T, pT>::setminReq(const dT& timesection, int request)
    { zm_minC.dur = timesection; zm_minC.cnt = request; }

    template<typename T, typename pT> inline constexpr void secureBIrandomT<T, pT>::setmaxReq(const dT& timesection, int request)
    { zm_maxC.dur = timesection; zm_maxC.cnt = request; }

    template<typename T, typename pT> inline constexpr void secureBIrandomT<T, pT>::setChange(const int& changeSpace, int change)
    { zm_changeSpace = changeSpace; zm_change = change; std::srand(mc::now().time_since_epoch().count()); }

}