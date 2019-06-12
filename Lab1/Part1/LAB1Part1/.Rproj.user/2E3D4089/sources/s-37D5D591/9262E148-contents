# sales1<-c(12,14,16,29,30,45,19,20,16, 19, 34, 20)
# sales2<-rpois(12,34)
# par(bg="cornsilk")
# plot(sales1, col = "blue", type = "o", ylim = c(0,100), xlab = "Month", ylab = "Sales")
# sales2<-rpois(12,34)
# par(bg="cornsilk")
 lines(sales2, type = "o", lty = 2, pch=22, col="red")
# grid(nx=NA, ny=NULL)
# legend("topright", inset=.05, c("Sales1","Sales2"), fill=c("blue","red"), horiz=TRUE)
# 
# sales <- read.table(file.choose(),header = T)
# sales
# barplot(as.matrix(sales), main="Sales Data", ylab= "Total",beside=T, col=rainbow(5))
# 
# fn<-boxplot(sales,col=c("orange","green"))$stats
# text(1.45, fn[3,2], paste("Median =", fn[3,2]), adj=0, cex=.7)
# text(0.45, fn[3,1],paste("Median =", fn[3,1]), adj=0, cex=.7)
# grid(nx=NA, ny=NULL)
# 

fb1<-read.csv(file.choose())
appl1<-read.csv(file.choose())
appl1$Adj.Close
par(bg="cornsilk")
plot(appl1$Adj.Close, col="blue", type="o", ylim=c(0,200), xlab="Days", ylab="Price" )
lines(fb1$Adj.Close, type="o", pch=22, lty=2, col="red")
legend("topright", inset=.05, c("Apple","Facebook"), fill=c("blue","red"), horiz=TRUE)
#Just study the distribution of the adjusted close of the stock price of Apple.
hist(aapl1$Adj.Close, col=rainbow(8))


install.packages("ggplot2")

data()
#Observe the data sets available for explorations.
library(ggplot2)
attach(mpg)
head(mpg)
summary(mpg)
#after analysis remove the data from the memory
detach(mpg)

#Also explore newer data sets in 
library (help=datasets)
library(datasets)
head(uspop)
plot(uspop)


install.packages("devtools")

library("ggmap")
library("maptools")
library("maps")
register_google(key = 'AIzaSyCg_VUNB8jbTN5tQjdUCEBig-QwsLlhx2s') 
#devtools::install_github("dkahle/ggmap") #,ref = "tidyup")
visited <- c("SFO", "Chennai", "London", "Melbourne", "Lima, Peru", "Johannesbury, SA")
ll.visited <- geocode(visited)
visit.x <- ll.visited$lon
visit.y <- ll.visited$lat
map("world", fill=TRUE, col="white", bg="lightblue", ylim=c(-60, 90), mar=c(0,0,0,0))
points(visit.x,visit.y, col="red", pch=36)

attach(mtcars)
head(mtcars)
mtcars[c(1,3,4,5,6)]
plot(mtcars[c(1,3,4,5,6)], main="MTCARS Data")
plot(mtcars[c(1,3,4,6)], main="MTCARS Data")
plot(mtcars[c(1,3,4,6)], col=rainbow(5),main="MTCARS Data")
detach(mtcars)

library(ggplot2)
ggplot(mtcars, aes(x=mpg, y=disp)) + geom_point()
