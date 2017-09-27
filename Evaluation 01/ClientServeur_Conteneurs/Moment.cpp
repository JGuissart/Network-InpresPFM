#include <stdlib.h>
#include <time.h> // Fonction time(...), localtime(...), mktime(...)
#include <stdio.h>
#include "Moment.h"

using namespace std;

Moment::Moment()
{
    setValue(0);
    setDate(false);
}

Moment::Moment(int duree)
{
    Value = duree * 60;
    bDate = false;
}

Moment::Moment(int y, int m, int d)
{
    struct tm x;

    x.tm_year = y - 1900;
    x.tm_mon = m - 1;
    x.tm_mday = d;
    x.tm_hour = 0;
    x.tm_min = 0;
    x.tm_sec = 0;
    x.tm_isdst = 1;
    Value = mktime(&x);

    bDate = true;
}

Moment::Moment(int y, int mon, int d, int h, int min)
{
    struct tm x;

    x.tm_year = y - 1900;
    x.tm_mon = mon - 1;
    x.tm_mday = d;
    x.tm_hour = h;
    x.tm_min = min;
    x.tm_sec = 0;
    x.tm_isdst = 1;
    Value = mktime(&x);

    bDate = true;
}

Moment::Moment(const Moment &m)
{
    setValue(m.getValue());
    setDate(m.IsDate());
}

Moment::~Moment()
{

}

void Moment::setValue(int v)
{
    Value = v;
}

void Moment::setDate(bool d)
{
    bDate = d;
}

void Moment::setAnnee(int y)
{
    struct tm *p;

    p = localtime((time_t *) &Value);

    p->tm_year = y - 1900;

    Value = mktime(p);
}

void Moment::setMois(int m)
{
    struct tm *p;

    p = localtime((time_t *) &Value);

    p->tm_mon = m - 1;

    Value = mktime(p);
}

void Moment::setJour(int j)
{
    struct tm *p;

    p = localtime((time_t *) &Value);

    p->tm_mday = j;

    Value = mktime(p);
}
void Moment::setHeure(int h)
{
    struct tm *p;

    p = localtime((time_t *) &Value);

    p->tm_hour = h;

    Value = mktime(p);
}
void Moment::setMinute(int m)
{
    struct tm *p;

    p = localtime((time_t *) &Value);

    p->tm_min = m;

    Value = mktime(p);
}
void Moment::setSeconde(int s)
{
    struct tm *p;

    p = localtime((time_t *) &Value);

    p->tm_sec = s;

    Value = mktime(p);
}
int Moment::getValue() const
{
    return Value;
}
bool Moment::IsDate() const
{
    return bDate;
}
int Moment::getAnnee() const
{
    struct tm *p;

    p = localtime((time_t *) &Value);

    return p->tm_year + 1900;
}
int Moment::getMois() const
{
    struct tm *p;

    p = localtime((time_t *) &Value);

    return p->tm_mon + 1;
}
int Moment::getJour() const
{
    struct tm *p;

    p = localtime((time_t *) &Value);

    return p->tm_mday;
}
int Moment::getHeure() const
{
    struct tm *p;

    p = localtime((time_t *) &Value);

    return p->tm_hour;
}
int Moment::getMinute() const
{
    struct tm *p;

    p = localtime((time_t *) &Value);

    return p->tm_min;
}
int Moment::getSeconde() const
{
    struct tm *p;

    p = localtime((time_t *) &Value);

    return p->tm_sec;
}
string Moment::ToString() const
{
    char buf[40];

    if (bDate)
        sprintf(buf, "%d/%d/%d %d:%d:%d", getJour(), getMois(), getAnnee(), getHeure(), getMinute(), getSeconde());
    else
    {
        int h, min, s;

        s = getValue();
        if (s < 0)
            s = -s;

        h = s / 3600;
        s = s % 3600;
        min = s / 60;
        s = s % 60;

        if (getValue() >= 0)
            sprintf(buf, "%d:%d:%d", h, min, s);
        else
            sprintf(buf,"- %d:%d:%d", h, min, s);
    }

    return string(buf);
}
Moment Moment::Now()
{
    Moment m;

	m.setDate(true);
	m.setValue(time(NULL));

	return m;
}
Moment& Moment::operator=(const Moment &m)
{
    Value = m.Value;
    bDate = m.bDate;

    return *this;
}
Moment Moment::operator+(int min)
{
    Moment m(*this);

    m.Value = m.Value + min * 60;

    return m;
}
Moment Moment::operator-(int min)
{
    Moment m(*this);

    m.Value = m.Value - min * 60;

    return m;
}
Moment Moment::operator+(const Moment &d)
{
    Moment m(*this);

    if (m.IsDate() && d.IsDate())
    {
        // --> Exception
    }
    else
    {
        m.Value += d.Value;

        if((m.IsDate() && !(d.IsDate())) || (!(m.IsDate()) && d.IsDate()))
            m.setDate(true);
        else
            m.setDate(false);
    }

    return m;
}
Moment Moment::operator-(const Moment &d)
{
    Moment m(*this);

    if (m.IsDate() && d.IsDate())
    {
        m.Value -= d.Value;
        m.setDate(false);
    }
    if (m.IsDate() && !(d.IsDate()))
        m.Value -= d.Value;

    if (!(m.IsDate()) && !(d.IsDate()))
        m.Value -= d.Value;

    return m;
}
ostream &operator<<(ostream &o, const Moment &m)
{
    o << m.ToString();

    return o;
}
bool Moment::operator<(const Moment &m)
{
    if (Value < m.Value)
        return true;
    else
        return false;
}
bool Moment::operator>(const Moment &m)
{
    if (Value > m.Value)
        return true;
    else
        return false;
}
bool Moment::operator==(const Moment &m)
{
    if (Value == m.Value)
        return true;
    else
        return false;
}
Moment Moment::operator++()
{
    int ValueTemp = this->Value;
    struct tm *p;

    p = localtime((time_t *) &ValueTemp);

    p->tm_mday = p->tm_mday + 1;

    ValueTemp = mktime(p);

    this->Value = ValueTemp;

    return *this;
}
Moment Moment::operator++(int)
{
    Moment temp(*this);
    int ValueTemp = this->Value;
    struct tm *p;

    p = localtime((time_t *) &ValueTemp);

    p->tm_mday = p->tm_mday + 1;

    ValueTemp = mktime(p);

    this->Value = ValueTemp;

    return temp;
}

