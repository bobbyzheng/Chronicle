import sys
import calendar

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
    def __init__(self, name="Calendar"):
        self.name = name
        self.events = []

    def addEvent(self, event):
        self.events.append(event)

    def getEventsByDay(self, date):
        matched_events = []
        for event in self.events:
            if event.day == date.day and event.month == date.month and event.year == date.year:
                matched_events.append(event)
        
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
            print(day)
            matched_events.extend(self.getEventsByDay(Date(day, date.month, date.year)))

        return matched_events

class Event:
    def __init__(self, title, date, time):
        self.title = title
        self.day = date.day
        self.month = date.month
        self.year = date.year
        self.starttime = time.start
        self.stoptime = time.stop
    
e = Event("Class", Date(1,1,2012), TimeBlock(12,2))
e1 = Event("Class2", Date(2,1,2012), TimeBlock(12,2))
c = CalendarApp()
c.addEvent(e)
c.addEvent(e1)

events = c.getEventsByWeek(Date(1,1,2012))
for each in events:
    print(each.title)

