# Graph 1
library(ggplot2)
library(reshape2)
publicData <- read.csv(file.choose(), skip = 1)
dummy_data <- data.frame('REGION.TYPE'=NA, 'REGION'=NA, 'YEAR'=2019, 'WEEK'=c(9:18), 'TOTAL.SPECIMENS'=NA, 'A..2009.H1N1.'=NA, 'A..H3.'=NA, 'A..Subtyping.not.Performed.'=NA, 'B'=NA, 'BVic'=NA, 'BYam'=NA, 'H3N2v'=NA)
publicData <- rbind(publicData, dummy_data)
week_below_10 <-  publicData[which(publicData$WEEK < 10), ]$WEEK
week_below_10 <- paste("0", week_below_10, sep="")
publicData[which(publicData$WEEK < 10), ]$WEEK <- week_below_10
labels_X <- gsub(" ", "", paste(publicData$YEAR, publicData$WEEK), fixed = TRUE)
#publicData$WEEK <- factor(publicData$WEEK, levels = publicData$WEEK ,ordered = TRUE)
publicData_2018 <- publicData[which(publicData$YEAR == 2018 & publicData$WEEK >= 40), ]
publicData_2019 <- publicData[which(publicData$YEAR == 2019), ]
filtered_data <- rbind(publicData_2018,publicData_2019)

filtered_data$WEEK = factor(filtered_data$WEEK, levels=filtered_data$WEEK)
labels_X <- gsub(" ", "", paste(filtered_data$YEAR, filtered_data$WEEK), fixed = TRUE)
labels_X <- labels_X[seq(1, length(labels_X), 2)]
labels_X_modified <- c()
for (label in labels_X) {
  labels_X_modified <- c(labels_X_modified, c(label," "))
}


dfm <- melt(filtered_data[,c('WEEK', 'A..Subtyping.not.Performed.', 
                          'A..2009.H1N1.', 'A..H3.', 'H3N2v', 'B', 'BVic', 'BYam')],id.vars = 1)
legend_labels <- c("A..Subtyping.not.Performed." = "A (subtyping not performed)",
                   "A..2009.H1N1." = "A (H1N1)pdm09",
                   "A..H3." = "A (H3N2)", 
                   "H3N2v" = "H3N2v", 
                   "B" = "B (lineage not performed)",
                   "Bvic" = "B (Victoria Lineage)",
                   "BYam" = "B (Yamagata Lineage)")
legend_colors <- c("A..Subtyping.not.Performed." = "#ffff66",
                   "A..2009.H1N1." = "#ff9900",
                   "A..H3." = "#ff0000", 
                   "H3N2v" = "blue", 
                   "B" = "green",
                   "Bvic" = "pink",
                   "BYam" = "black")
ggplot(dfm,aes(x = WEEK,y = value)) + 
  geom_bar(aes(fill = variable),stat = "identity",position =position_stack(reverse = FALSE))+
  labs(title = "Influenza Positive Tests Reported to CDC by U.S Clinical Laborities, National Summary, 2018-2019 Season", y = "Number of Positive Specimens", x = "Week", linetype = "My Line Name")+
  theme(panel.background = element_rect(fill = 'white', colour = 'black'))+
  scale_x_discrete(labels = labels_X_modified)+
  scale_y_continuous(limits = c(0, 3000), breaks = seq(0,3000, by=500))+
  theme(axis.text.x = element_text(angle = 60, hjust = 1))+
  scale_fill_manual(values = legend_colors, labels= legend_labels)+
  theme(axis.text.x = element_text(angle = 60, hjust = 1),
        plot.title = element_text(hjust=0.5, face="bold", size=14),
        axis.title.x = element_text(face="bold"),
        axis.title.y = element_text(face="bold"),
        legend.position = c(0.8,0.8))+
  guides(fill=guide_legend(title=""))

# Graph 2
library(reshape2)
library(ggplot2)
clinicalLabs <- read.csv(file.choose(), skip =1)
clinicalLabs <- clinicalLabs[which((clinicalLabs$WEEK >= 4 & clinicalLabs$YEAR == 2018) | (clinicalLabs$WEEK <= 4 & clinicalLabs$YEAR == 2019)), ]

week_below_10 <-  clinicalLabs[which(clinicalLabs$WEEK < 10), ]$WEEK
week_below_10 <- paste("0", week_below_10, sep="")
clinicalLabs[which(clinicalLabs$WEEK < 10), ]$WEEK <- week_below_10
labels_X <- gsub(" ", "", paste(clinicalLabs$YEAR, clinicalLabs$WEEK), fixed = TRUE)

labels_X <- labels_X[seq(1, length(labels_X))]

clinicalLabs$XLabels = factor(labels_X, levels = labels_X)


dfm_clinical_bar <- melt(clinicalLabs[,c('XLabels', 'TOTAL.A', 'TOTAL.B', 'PERCENT.POSITIVE', 'PERCENT.A', 'PERCENT.B')],id.vars = 1)
#dfm_clinical_bar$WEEK <- factor(dfm_clinical_bar$WEEK, levels = dfm_clinical_bar[which(dfm_clinical_bar$variable == 'TOTAL.A'), ]$WEEK)
percent_positive <- dfm_clinical_bar[which(dfm_clinical_bar$variable == 'PERCENT.POSITIVE'), ]
percent_positive$value <- percent_positive$value/0.0025
percent_A <- dfm_clinical_bar[which(dfm_clinical_bar$variable == 'PERCENT.A'), ]
percent_A$value <- percent_A$value/0.0025
percent_B <- dfm_clinical_bar[which(dfm_clinical_bar$variable == 'PERCENT.B'), ]
percent_B$value <- percent_B$value/0.0025

line_data <- dfm_clinical_bar[which(dfm_clinical_bar$variable == 'PERCENT.POSITIVE' | dfm_clinical_bar$variable == 'PERCENT.A' | dfm_clinical_bar$variable == 'PERCENT.B'), ]
line_data$value <- line_data$value/ 0.0025



bar_data <- dfm_clinical_bar[which(dfm_clinical_bar$variable =='TOTAL.A' | dfm_clinical_bar$variable =='TOTAL.B'), ]
legend_labels <- c("TOTAL.A" = "A", "TOTAL.B" = "B", "PERCENT.A" = "% positive Flu A", "PERCENT.B" = "% positive Flu B", "PERCENT.POSITIVE" = "Percent positive")
bargraph_colors <- c("TOTAL.A" = "#FEE053", "TOTAL.B" = "#2E6B2F", "PERCENT.A" = "#FFFFFF", "PERCENT.B" = "#FFFFFF", "PERCENT.POSITIVE" = "#FFFFFF")
linegraph_colors <- c("TOTAL.A" = "#FEE053", "TOTAL.B" = "#2E6B2F", "PERCENT.B" = "#ff3333", "PERCENT.A" = "#ff00ff", "PERCENT.POSITIVE" = "#000000")

ggplot(clinicalLabs,  aes(x = XLabels,y = value, group = variable, fill = variable, colour=variable))+ 
geom_bar(data =bar_data, stat = "identity")+
geom_line( data = percent_positive)+
geom_line( linetype = "dotted", data = percent_A)+
geom_line( linetype = "dotted", data = percent_B)+
scale_color_manual(values=linegraph_colors, labels= legend_labels)+
scale_fill_manual( values = bargraph_colors, labels= legend_labels)+
scale_y_continuous(sec.axis = sec_axis(~.*0.0025, name = "Percent Positive", breaks = seq(0,35, by=5)),  
                   limits = c(0, 28000), breaks = seq(0,28000, by=2000), expand = c(0, 0))+
  labs(title = "Influenza Positive Tests Reported to CDC by U.S Clinical Laborities, National Summary, 2018-2019 Season", y = "Number of Positive Specimens", x = "Week", linetype = "My Line Name")+
  theme(axis.line = element_line(colour = "#000000", linetype = "solid",size = 0.4),
        panel.background = element_rect(fill = 'white', colour = 'black'))+
  scale_x_discrete(breaks = levels(clinicalLabs$XLabels)[c(T, F)], expand = c(0, 0))+
  theme(axis.text.x = element_text(angle = 60, hjust = 1),
        plot.title = element_text(hjust=0.5, face="bold", size=14),
        axis.title.x = element_text(face="bold"),
        axis.title.y = element_text(face="bold"),
        legend.position = c(0.8,0.8))
  
 

# Graph 3
library(usmap)
library(ggplot2)
library(dplyr)
heatmapData <- read.csv(file.choose())
heatmapData <- heatmapData[grep("Level ", heatmapData$ACTIVITY.LEVEL),]
heatmapData$LevelInt <- as.integer(substr(heatmapData$ACTIVITY.LEVEL, 7, 9))
agreegated_data <- aggregate(heatmapData, by = list(heatmapData$STATENAME), FUN = mean)
agreegated_data$LevelInt <- ceiling(agreegated_data$LevelInt)
agreegated_data$LevelInt <- paste("Level", agreegated_data$LevelInt)

code <- fips(agreegated_data[, c('Group.1')])
value <- agreegated_data$LevelInt
df <- data.frame("fips" = code, "LevelInt" = value)
df
pal <- c(
  "Level 10" = "#CC0000",
  "Level 9" = "#FA4F00",
  "Level 8" = "#FC8200",
  "Level 7" = "#FCB100",
  "Level 6" = "#F7DF00",
  "Level 5" = "#E0F500",
  "Level 4" = "#BAF700",
  "Level 3" = "#8CF700",
  "Level 2" = "#5BF700",
  "Level 1" = "#00C200",
  "Level 0" = "#ffffff"
  
)   
plot_usmap(data = df, values = "LevelInt", lines = "black")+
  scale_fill_manual(values = pal, limits = names(pal), name='ILI Activity Level')+
  labs(title = "Average activity level data for 2018-19 \n Influenza Season from week 40 ending Oct 10, 2018 to week 4 Jan 26, 2019")+
  theme(legend.position = "right", plot.title = element_text(hjust=0.5))


# Graph 4
library(dplyr)
out_patient_data <- read.csv(file.choose())

df <- out_patient_data[, c('X..WEIGHTED.ILI','WEEK', 'YEAR')]
df$WEEK <- factor(df$WEEK, levels = unique(df$WEEK[match(40,df$WEEK): length(df$WEEK)]), ordered = TRUE)
years <- sort(unique(df$YEAR))

# Constructing all line graphs
i<-1
p <- ggplot(df)
data_total <- data.frame()
while (i < length(years)) {
  if(years[i] == 2012 || years[i] == 2013) { i = i+1; next;}
  if(i +1 > length(years)) break;
  data_temp1 <- df[which((df$YEAR == years[i] & df$WEEK >=40 & df$WEEK <= 52)), ]
  data_temp2 <- df[which((df$YEAR == years[i+1]& df$WEEK >= 1 & df$WEEK != 53)), ]
  data_temp <- rbind(data_temp1, data_temp2)
  data_temp$season <- paste(toString(years[i]), "-", toString(years[i+1] - 2000), " season")
  p <- p + geom_line(data = data_temp[, c('WEEK', 'X..WEIGHTED.ILI', 'season')],
                     aes(x = WEEK,y = X..WEIGHTED.ILI, group = season, color=season))
  if(years[i] == 2018){
    p <- p + geom_point(data=data_temp[, c('WEEK', 'X..WEIGHTED.ILI',  'season')], shape = 24,aes(data_temp$WEEK, data_temp$X..WEIGHTED.ILI,group = season, color=season))
  }
  i = i+1
}
lineColors <- c(`2017-18 season` = "#ACFFFF", `2016-17 season` = "#3A45F6", `2015-16 season` = "#F78A5B", 
                `2014-15 season` = "#E489E3", `2011-12 season` = "#58FF55", `2009-10 season` = "#BDBFBC", 
                `2018-19 season` = "#E20F11", `National baseline` = "#000000")
fillColors <- c(`2017-18 season` = "#FFFFFF", `2016-17 season` = "#FFFFFF", `2015-16 season` = "#FFFFFF", 
                `2014-15 season` = "#FFFFFF", `2011-12 season` = "#FFFFFF", `2009-10 season` = "#FFFFFF", 
                `2018-19 season` = "#FFFFFF", `National baseline` = "#FFFFFF")
# Formating X-axis labels
lbl1 <- c(40:52)
lbl1 <- lbl1[which(lbl1 %% 2 == 0)]
lbl2 <- c(1:39)
lbl2 <- lbl2[which(lbl2 %% 2 == 0)]
lbl <- append(lbl1, lbl2)
labels_X = c()

for (label in lbl) {
  labels_X <- c(labels_X, c(label," "))
}
# creating National baseline dataframe
data2 <- data.frame()

national_baseline <- data.frame("WEEK1" = append(c(40:52), c(1:39)), 'X..WEIGHTED.ILI1' = 2.2, 'season1' = 'National Baseline' )
data2 <- rbind(data2, national_baseline)
p +geom_line(linetype = 2, data = data2,
             aes(x = data2$WEEK1,y = data2$X..WEIGHTED.ILI1, color=data2$season1))+
   scale_colour_manual(name = "", values = lineColors, guide = "legend")+
 scale_fill_manual(name = "", values = fillColors, guide = "legend")+
scale_shape_manual(name = "", values = c(NA, NA, NA, NA, NA, NA, NA,
                                           24, NA), guide = "legend")+
scale_linetype_manual(name = "", values = c("solid", "solid", "solid", 
                                              "solid", "solid", "solid","solid", "solid", "dashed"), guide = "legend")+
  scale_x_discrete(labels = labels_X) +
  scale_y_continuous(breaks =c(0:8))+
  labs(title = "Percentage of visits for Influenza-like Illness (ILI) reported by the U.S. Outpatient Influenza-like Illness Surveillance Network (ILINet),\n Weekly National Summary, 2018-2019 and Selected Previous Seasons") + xlab("Week") + ylab("% of Visits for ILI")+

  theme(legend.box.background = element_rect(fill = 'white', colour = "black", size=2), 
        panel.background = element_rect(fill = 'white', colour = 'black'),
        plot.title = element_text(hjust=0.5, face="bold", size=14),
        axis.title.x = element_text(face="bold"),
        axis.title.y = element_text(face="bold"),
        legend.position = c(0.8,0.8))



library(ggplot2)
library(reshape2)
library(dplyr)
library(shiny)
ped_death <- read.csv(file.choose(), skip = 1)

levels_data <- levels(ped_death$WEEK.NUMBER)[c(T,F,F,F,F,F)]
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
  
  labs( title = "Number of Influenza Associated Pediatric Deaths by \n Week of Death 2015-2016 season to present",  color = "Legend Title\n") + xlab("Week of Death") + ylab("Number of Deaths")+
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
