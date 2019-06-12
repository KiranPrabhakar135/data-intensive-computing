library(dplyr)
out_patient_data <- read.csv(file.choose())

df <- out_patient_data[, c('X..WEIGHTED.ILI','WEEK', 'YEAR')]
df$WEEK <- factor(df$WEEK, levels = unique(df$WEEK[match(40,df$WEEK): length(df$WEEK)]), ordered = TRUE)
years <- sort(unique(df$YEAR))

# Constructing all line graphs
i<-1
data_temp1 <- df[which((df$YEAR == years[6] & df$WEEK >=40 & df$WEEK <= 52)), ]
data_temp2 <- df[which((df$YEAR == years[6+1]& df$WEEK >= 1 & df$WEEK != 53)), ]
data_temp <- rbind(data_temp1, data_temp2)
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
  i = i+1
}

code <- fips(heatmapData[, c('STATENAME')])
value <- heatmapData[, c('ACTIVITY.LEVEL')]
df <- data.frame("fips" = code, "ACTIVITY.LEVEL" = value)
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
plot_usmap(data = df, values = "ACTIVITY.LEVEL", lines = "black")+
  scale_fill_manual(values = pal, limits = names(pal), name='ILI Activity Level')+
  labs(title = "Western US States", subtitle = "These are the states in the Pacific Timezone.")+
  theme(legend.position = "right")

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
national_baseline <- data.frame("WEEK" = append(c(40:52), c(1:39)), 'X..WEIGHTED.ILI' = 2.2, 'season' = 'National Baseline' )
p +geom_line(linetype = 2, data = national_baseline,
             aes(x = WEEK,y = X..WEIGHTED.ILI, group = season, color=season))+
  #scale_color_manual(values = c(national_baseline= 'black'))+
  scale_x_discrete(labels = labels_X) +
  scale_y_continuous(breaks =c(0:8))+
  labs(title = "Percentage of visits for Influenza-like Illness (ILI) reported by the U.S. Outpatient Influenza-like Illness Surveillance Network (ILINet),\n Weekly National Summary, 2018-2019 and Selected Previous Seasons") + xlab("Week") + ylab("% of Visits for ILI")+
  theme(panel.background = element_rect(fill = 'white', colour = 'black'))