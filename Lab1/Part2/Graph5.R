library(ggplot2)
library(reshape2)
library(dplyr)
library(shiny)
ped_death <- read.csv(file.choose(), skip = 1)

levels_data <- levels(ped_death$WEEK.NUMBER)[c(T,F, F,F, F, F)]
labels_X <- c()
for (label in levels_data) {
  labels_X <- c(labels_X, c(label,":"))
}
sum <- aggregate(ped_death$NO..OF.DEATHS, by = list(Category=ped_death$SEASON), FUN=sum)
deaths <- sum$x
category <- sum$Category

dfm <- melt(ped_death[,c('WEEK.NUMBER', 'PREVIOUS.WEEK.DEATHS', 'CURRENT.WEEK.DEATHS')],id.vars = 1)
dfm$legend = dfm$variable
ggplot(dfm) + 
  geom_bar(aes(x = WEEK.NUMBER,y = value, fill = legend), colour="black", stat = "identity",position =position_stack(reverse = TRUE))+
  scale_x_discrete(breaks = labels_X, labels = labels_X, expand=c(0,0))+
  theme(axis.text.x = element_text(angle = 90, hjust = 1))+
  scale_y_continuous(breaks =seq(from = 0, to = 30, by = 5), expand=c(0,0))+
  coord_cartesian(ylim=c(0,30))+
  scale_fill_manual(labels = c("Deaths reported previous week", "Deaths reported current week"),values = c(PREVIOUS.WEEK.DEATHS= 'green4',CURRENT.WEEK.DEATHS = 'cyan' ))+
  
  labs( title = "Number of Influenza Associated Pediatric Deaths by Week of Death 2015-2016 season to present",  color = "Legend Title\n") + xlab("Week of Death") + ylab("Number of Deaths")+
  theme(legend.box.background = element_rect(fill = 'white', colour = "black", size=2), panel.background = element_rect(fill = 'white', colour = 'black'), legend.position='bottom',
        plot.title = element_text(hjust=0.5, face="bold", size=14),
        axis.title.x = element_text(face="bold"),
        axis.title.y = element_text(face="bold"))+
  annotate("text", x = 25, y = 25, label = paste(category[1]), fontface ='bold')+
  annotate("text", x = 25, y = 22, label = paste("Number of Deaths \n Reported =", deaths[1]))+
  annotate("text", x = 75, y = 25, label = paste( category[2]), fontface ='bold')+
  annotate("text", x = 75, y = 22, label = paste("Number of Deaths \n Reported =", deaths[2]))+
  annotate("text", x = 125, y = 25, label = paste(category[3]), fontface ='bold')+
  annotate("text", x = 125, y = 22, label = paste("Number of Deaths \n Reported =", deaths[3]))+
  annotate("text", x = 165, y = 25, label = paste(category[4]), fontface ='bold')+
  annotate("text", x = 165, y = 22, label = paste("Number of Deaths \n Reported =", deaths[4]))
