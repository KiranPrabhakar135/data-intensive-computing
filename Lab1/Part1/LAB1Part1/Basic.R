# func_arguments <- function(a=1,b=2,c=3) {
#   res <- a + (b * c)
#   print(res)
# }

function1 <- function(a=1,b=2){
  res <- (a+b)
  print(res)
  mydata <- data.frame(stu_id = c(1:5), stu_name=c('a','b', 'c', 'asdf', 'asf'));
  print(mydata);  
}

function1()
