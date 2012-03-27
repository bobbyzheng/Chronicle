import sys
import calendar

def isDateAfter(d1, d2):
    #is date d1 after date d2
    if d1.year < d2.year: return False
    if d1.year >= d2.year and d1.month < d2.month: return False
    if d1.year >= d2.year and d1.month >= d2.month and d1.day < d2.day: return False
    
    return True

class Date:
    def __init__(self, day, month, year):
        self.day = day
        self.month = month
        self.year = year

class TimeBlock:
    def __init__(self, start, stop):
        self.start = start
        self.stop = stop

class CalendarApp:
    def __init__(self):
        self.events = []
        self.reoccuringevents = []

    def addEvent(self, event):
        if event.reoccuring == False:
            self.events.append(event)
        else:
            self.reoccuringevents.append(event)

    def getEventsByDay(self, date):
        matched_events = []
        for event in self.events:
            if event.day == date.day and event.month == date.month and event.year == date.year:
                matched_events.append(event)

        for event in self.reoccuringevents:
            if event.period == "daily":
                if isDateAfter(date, event) is True: 
                    if event.stopdate is not None and isDateAfter(event.stopdate, date) is True:
                        tmp_event = Event(event.title, date, TimeBlock(event.starttime, event.stoptime))
                        matched_events.append(tmp_event)  
                    elif event.stopdate is None:
                        tmp_event = Event(event.title, date, TimeBlock(event.starttime, event.stoptime))
                        matched_events.append(tmp_event)  

            elif event.period == "weekly":
                if isDateAfter(date, event) is True:
                    pass

            elif event.period == "biweekly":
                pass
            else:
                pass
        
        return matched_events

    def getEventsByWeek(self, date):
        matched_events = []
        c = calendar.Calendar(6)
        week = []
        for weeks in c.monthdays2calendar(date.year, date.month):
            (day, dayofweek) = weeks[0]
            if date.day >= day and date.day <= day+6:
                week = map(lambda x: x[0], weeks)
                break
        for day in week:
            matched_events.extend(self.getEventsByDay(Date(day, date.month, date.year)))

        return matched_events

    def getEventsByTitle(self, title):
        matched_events = []
        for each in self.events:
            if each.title == title:
                matched_events.append(each)

        return matched_events

class Event:
    #period is string for how often event will occur
    def __init__(self, title, date, time, reoccuring=False, period="", stopdate=None):
        self.title = title
        self.day = date.day
        self.month = date.month
        self.year = date.year
        self.starttime = time.start
        self.stoptime = time.stop
        self.reoccuring = reoccuring
        self.period = period
        self.stopdate = stopdate


e = Event("Class", Date(1,1,2012), TimeBlock(12,2))
e1 = Event("Class2", Date(2,1,2012), TimeBlock(12,2))
e2 = Event("Class3", Date(1,1,2012), TimeBlock(2,4), True, "daily", Date(4,1,2012))
c = CalendarApp()
c.addEvent(e)
c.addEvent(e1)
c.addEvent(e2)

events = c.getEventsByWeek(Date(1,1,2012))
for each in events:
    print(each.title)
    print("\t", each.month, each.day, each.year)
    print("\t", each.starttime, each.stoptime)

