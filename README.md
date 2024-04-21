English is weird. How do you say the numer "101" out loud? One oh one? One hundred and one? Now think about normalizing how someone says all of this to the integer value. 

Not easy. 

So I wrote a function that manually generates all the ways I could think of for someone to say an integer that is less than places long.

Is this code optimized? Not by a long shot. Is it complete? Nah. Is it a starting place for what I was trying to solve for? Yes. 

ex: 
println(expandedConvert(10001))
> ["one zero zero zero one", "ten zero zero one", "ten thousand one", "one oh oh oh one", "ten oh oh one", "ten thousand and one"]
