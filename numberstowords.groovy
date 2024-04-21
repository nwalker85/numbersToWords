import java.text.DecimalFormat;


def convertLessThanOneThousand(int number) {
    def tensNames = [
		"",
		" ten",
		" twenty",
		" thirty",
		" forty",
		" fifty",
		" sixty",
		" seventy",
		" eighty",
		" ninety"];

	def numNames = [
		"",
		" one",
		" two",
		" three",
		" four",
		" five",
		" six",
		" seven",
		" eight",
		" nine",
		" ten",
		" eleven",
		" twelve",
		" thirteen",
		" fourteen",
		" fifteen",
		" sixteen",
		" seventeen",
		" eighteen",
		" nineteen"
	];
    
    String soFar;

    if (number % 100 < 20){
      soFar = numNames[number % 100];
      number /= 100;
    }
    else {
      soFar = numNames[number % 10];
      number /= 10;

      soFar = tensNames[number % 10] + soFar;
      number /= 10;
    }
    if (number == 0) return soFar;
    return numNames[number] + " hundred" + soFar;
  }


def convert(long number) {

	def tensNames = [
		"",
		" ten",
		" twenty",
		" thirty",
		" forty",
		" fifty",
		" sixty",
		" seventy",
		" eighty",
		" ninety"];

	def numNames = [
		"",
		" one",
		" two",
		" three",
		" four",
		" five",
		" six",
		" seven",
		" eight",
		" nine",
		" ten",
		" eleven",
		" twelve",
		" thirteen",
		" fourteen",
		" fifteen",
		" sixteen",
		" seventeen",
		" eighteen",
		" nineteen"
	];

    // 0 to 999 999 999 999
    if (number == 0) { return "zero"; }

    String snumber = Long.toString(number);

    // pad with "0"
    String mask = "000000000000";
    DecimalFormat df = new DecimalFormat(mask);
    snumber = df.format(number);

    // XXXnnnnnnnnn
    int billions = Integer.parseInt(snumber.substring(0,3));
    // nnnXXXnnnnnn
    int millions  = Integer.parseInt(snumber.substring(3,6));
    // nnnnnnXXXnnn
    int hundredThousands = Integer.parseInt(snumber.substring(6,9));
    // nnnnnnnnnXXX
    int thousands = Integer.parseInt(snumber.substring(9,12));

    String tradBillions;
    switch (billions) {
    case 0:
      tradBillions = "";
      break;
    case 1 :
      tradBillions = """${convertLessThanOneThousand(billions)} billion """;
      break;
    default :
      tradBillions = """${convertLessThanOneThousand(billions)} billion """;
    }
    String result =  tradBillions;

    String tradMillions;
    switch (millions) {
    case 0:
      tradMillions = "";
      break;
    case 1 :
      tradMillions = """${convertLessThanOneThousand(millions)} million """;
      break;
    default :
      tradMillions = """${convertLessThanOneThousand(millions)} million """;
    }
    result =  result + tradMillions;

    String tradHundredThousands;
    switch (hundredThousands) {
    case 0:
      tradHundredThousands = "";
      break;
    case 1 :
      tradHundredThousands = "one thousand ";
      break;
    default :
      tradHundredThousands = """${convertLessThanOneThousand(hundredThousands)} thousand """;
    }
    result =  result + tradHundredThousands;

    String tradThousand;
    tradThousand = convertLessThanOneThousand(thousands);
    result =  result + tradThousand;

    return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
    }

def addOhs(List wordList){

    Integer matchIndex = 0
    Integer size = wordList.size()
    List newAdditions = []
    Integer newIndex = size
    wordList.eachWithIndex{ it, i ->
        if(it=~/zero/){
            newAdditions.putAt(matchIndex, it.replace('zero', 'oh'))
            matchIndex += 1
        }
    }
    newAdditions.eachWithIndex{ it, i ->
        wordList[newIndex] = it
        newIndex += 1
    }
    return wordList
}

def addAnds(List wordList){

    def newAdditions = []
    wordList.eachWithIndex{ it, i ->
        if((it=~/hundred$/)||(it=~/thousand$/)){}
        else if((it=~/thousand/) && !(it=~/hundred/)){
            newAdditions.add(it.replace('thousand', 'thousand and'))
        }
        else if (it=~/hundred/){
            newAdditions.add(it.replace('hundred', 'hundred and'))
        }
    }   
    wordList.addAll(newAdditions)

    return wordList
}

def expandedConvert(int input) {

    def num = input.toString().collect { it as int }
    def digit
    def doublet
    def doublet1
    def doublet2
    def digit1
    def digit2
    def digit3
    def triplet
    def quadruplet
    def phrase
    def response = []
    def response_index = 0
    def skip_flag = false

    switch(num.size()){
        case 1:
            response.add("""\"${convert(num[0])}\"""")
            break

        case 2:

            //digit digit
            response.add("""\"${convert(num[0])} ${convert(num[1])}\"""")

            //doublet
            if(num[0]==0){
                    response.add("""\"zero ${convert(num[1])}\"""")
            }
            else{
                doublet = Long.valueOf("""${num[0]}${num[1]}""")
                response.add("""\"${convert(doublet)}\"""")
            }
            break

        case 3:

            //digit digit digit
            response.add("""\"${convert(num[0])} ${convert(num[1])} ${convert(num[2])}\"""")
            
            //digit doublet
            if(num[1]!=0){
                doublet = convert(Long.valueOf("""${num[1]}${num[2]}"""))
                digit = convert(num[0])
                response.add("""\"${digit} ${doublet}\"""")
            }

            //triplet
            response.add("""\"${convert(Long.valueOf("""${num[0]}${num[1]}${num[2]}"""))}\"""")
            break


        case 4:

            //digit digit digit digit
            response.add("""\"${convert(num[0])} ${convert(num[1])} ${convert(num[2])} ${convert(num[3])}\"""")

            //doublet doublet
            if((num[0]!=0)&&(num[2]!=0)){
                if(num[1]==0){
                    if(num[0]==1){
                    doublet1 = convert(Long.valueOf("""${num[0]}${num[1]}"""))
                    doublet2 = convert(Long.valueOf("""${num[2]}${num[3]}"""))
                    response.add("""\"${doublet1} ${doublet2}\"""")
                    }
                }
                doublet1 = convert(Long.valueOf("""${num[0]}${num[1]}"""))
                doublet2 = convert(Long.valueOf("""${num[2]}${num[3]}"""))
                response.add("""\"${doublet1} ${doublet2}\"""")
            }

            //doublet digit digit
            if(num[0]!=0){
                if(num[1]==0){
                    if(num[1]==1){
                        doublet = convert(Long.valueOf("""${num[0]}${num[1]}"""))
                        digit1 = convert(num[2])
                        digit2 = convert(num[3])
                        response.add("""\"${doublet} ${digit1} ${digit2}\"""")
                    }
                }
                doublet = convert(Long.valueOf("""${num[0]}${num[1]}"""))
                digit1 = convert(num[2])
                digit2 = convert(num[3])
                response.add("""\"${doublet} ${digit1} ${digit2}\"""")
            }

            //digit digit doublet
            if(num[2]==0){}
            else{
                doublet = convert(Long.valueOf("""${num[2]}${num[3]}"""))
                digit1 = convert(num[0])
                digit2 = convert(num[1])
                response.add("""\"${digit1} ${digit2} ${doublet}\"""")
            }

            //quadruplet
            response.add("""\"${convert(Long.valueOf("""${num[0]}${num[1]}${num[2]}${num[3]}"""))}\"""")
            
            //1100 -> "eleven hundred"
            if((num[2]==0)&&(num[3]==0)){
                doublet = convert(Long.valueOf("""${num[0]}${num[1]}"""))
                response.add("""\"${doublet} hundred\"""")
            }
            
            break


        case 5:
            //digit digit digit digit digit
            response.add("""\"${convert(num[0])} ${convert(num[1])} ${convert(num[2])} ${convert(num[3])} ${convert(num[4])}\"""")

            //doublet digit digit digit digit
            if(num[0]==0){}
            else{
                if(num[1]==0){
                    if(num[0]==1){
                        doublet = convert(Long.valueOf("""${num[0]}${num[1]}"""))
                        digit1 = convert(num[2])
                        digit2 = convert(num[3])
                        digit3 = convert(num[4])
                        response.add("""\"${doublet} ${digit1} ${digit2} ${digit3}\"""")
                    }
                }
                else{
                        doublet = convert(Long.valueOf("""${num[0]}${num[1]}"""))
                        digit1 = convert(num[2])
                        digit2 = convert(num[3])
                        digit3 = convert(num[4])
                        response.add("""\"${doublet} ${digit1} ${digit2} ${digit3}\"""")
                }
            }

            //digit doublet digit digit
            if(num[1]==0){}
            else{
                if(num[2]==0){
                    if(num[1]==1){
                        doublet = convert(Long.valueOf("""${num[1]}${num[2]}"""))
                        digit1 = convert(num[0])
                        digit2 = convert(num[3])
                        digit3 = convert(num[4])
                        response.add("""\"${digit1} ${doublet} ${digit2} ${digit3}\"""")
                    }
                }
                else{
                    doublet = convert(Long.valueOf("""${num[1]}${num[2]}"""))
                    digit1 = convert(num[0])
                    digit2 = convert(num[3])
                    digit3 = convert(num[4])
                    response.add("""\"${digit1} ${doublet} ${digit2} ${digit3}\"""")
                }
            }

            //digit digit doublet digit
            if(num[2]==0){}
            else{
                if(num[3]==0){
                    if(num[2]==1){
                        doublet = convert(Long.valueOf("""${num[2]}${num[3]}"""))
                        digit1 = convert(num[0])
                        digit2 = convert(num[1])
                        digit3 = convert(num[4])
                        response.add("""\"${digit1} ${digit2} ${doublet} ${digit3}\"""")
                    }
                }
                else{
                    doublet = convert(Long.valueOf("""${num[2]}${num[3]}"""))
                    digit1 = convert(num[0])
                    digit2 = convert(num[1])
                    digit3 = convert(num[4])
                    response.add("""\"${digit1} ${digit2} ${doublet} ${digit3}\"""")
                }
            }

            //digit digit digit doublet
            if(num[3]!=0){
                doublet = convert(Long.valueOf("""${num[3]}${num[4]}"""))
                digit1 = convert(num[0])
                digit2 = convert(num[1])
                digit3 = convert(num[2])
                response.add("""\"${digit1} ${digit2} ${digit3} ${doublet}\"""")
            }

            //doublet doublet digit
            if((num[0]==0)||(num[2]==0)){skip_flag = true}
            else{
                if((num[1]==0)&&(num[0]!=1)){skip_flag = true}
                if((num[3]==0)&&(num[2]!=1)){skip_flag = true}
            }
            if(skip_flag==false){                    
                doublet1 = convert(Long.valueOf("""${num[0]}${num[1]}"""))
                doublet2 = convert(Long.valueOf("""${num[2]}${num[3]}"""))
                digit = convert(num[4])
                response.add("""\"${doublet1} ${doublet2} ${digit}\"""")
            }
            skip_flag = false

            //doublet digit doublet
            if((num[0]==0)||(num[3]==0)){skip_flag = true}
            else{
                if((num[1]==0)&&(num[0]!=1)){skip_flag = true}
            }
            if(skip_flag==false){                    
                doublet1 = convert(Long.valueOf("""${num[0]}${num[1]}"""))
                doublet2 = convert(Long.valueOf("""${num[3]}${num[4]}"""))
                digit = convert(num[2])
                response.add("""\"${doublet1} ${digit} ${doublet2}\"""")
            }
            skip_flag = false
 

            //digit doublet doublet
            if((num[1]==0)||(num[3]==0)){skip_flag = true}
            else{
                if((num[2]==0)&&(num[1]!=1)){skip_flag = true}
            }
            if(skip_flag==false){                    
                doublet1 = convert(Long.valueOf("""${num[1]}${num[2]}"""))
                doublet2 = convert(Long.valueOf("""${num[3]}${num[4]}"""))
                digit = convert(num[0])
                response.add("""\"${digit} ${doublet1} ${doublet2}\"""")
            }
            skip_flag = false

            phrase = convert(Long.valueOf("""${num[0]}${num[1]}${num[2]}${num[3]}${num[4]}"""))
            response.add("\"$phrase\"")
            break

        default:
            response.add("error - too many digits")
        }
    response = addOhs(response)
    response = addAnds(response)
    //response = addAndsToThousands(response)
    return response.unique()
    } 