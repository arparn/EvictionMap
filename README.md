### My solution explanation

I use *concurrentHashMap* for my eviction map to make it thread safe.
I use *IllegalArgumentException*'s to control that user has passed correct 
data and avoid input errors. 
I use *Timer* to implement eviction policy.
To avoid resource leaks I create *TimerTask* every time I put value to the map
and immediately cancel the *TimerTask* when the task is done.
If all tasks is done *Timer* cancels as well.
Also I add *TimerTask* to *taskMap* in case I need to update existing 
values so I can find task by the same key I use for the value and cancel it to
start new one. After the task is done *TimerTask* and the key are deleted from 
*taskMap*. If the key-value pair exceeded the specified duration, 
then it expires and gets deleted from the map. Trying to get value which is expired
returns *null*.
