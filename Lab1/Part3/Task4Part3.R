library(datasets)
library(gridExtra)
library(ggplot2)
library(usmap)
library(ggplot2)
library(usmap)
library(ggplot2)
library(dplyr)

data_frame <- read.csv(file.choose())

state_name_df <- as.data.frame(state.name)
colnames(state_name_df) <- c('Var1')

freq_data <- as.data.frame(table(data_frame$state))

freq_data <-merge(state_name_df, freq_data, by='Var1', all.x = TRUE)
freq_data$Freq[which(is.na(freq_data$Freq))] <- 0

states <- freq_data$Var1
freq <- freq_data$Freq


code <- fips(states)
value <-freq
df_left <- data.frame("fips" = code, "Frequency" = value)
df_left <- df_left[which(!is.na(df_left$fips)), ]
  
left <- plot_usmap(data = df_left, values = "Frequency", lines = "black")+
  scale_fill_gradient2(low="#ffffff", mid="red", high="darkred",midpoint=(max(df_left$Frequency)+min(df_left$Frequency))/2,
                       limits=range(df_left$Frequency),name = "tweet count")+
  labs(title = "Fequency of flu related tweets for \n all keywords from each state")+
  theme(legend.position = "right", plot.title = element_text(hjust=0.5))

left



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
right <- plot_usmap(data = df, values = "LevelInt", lines = "black")+
  scale_fill_manual(values = pal, limits = names(pal), name='ILI Activity Level')+
  labs(title = "Average activity level data of each state \n from week 40, 2018 to week 4, 2019")+
  theme(legend.position = "right", plot.title = element_text(hjust=0.5))

grid.arrange(left,right, ncol=2)

