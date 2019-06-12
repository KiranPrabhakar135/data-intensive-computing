library(dplyr)
out_patient_data <- read.csv(file.choose(), skip = 1)
df <- out_patient_data[, c('X..WEIGHTED.ILI','WEEK', 'YEAR')]
df$WEEK <- factor(df$WEEK, levels = unique(df$WEEK[match(40,df$WEEK): length(df$WEEK)]), ordered = TRUE)
years <- sort(unique(df$YEAR))

# Preparing the data
i<-1
data_total <- data.frame()
while (i < length(years)) {
  if(years[i] == 2010 ||years[i] == 2012 || years[i] == 2013) { i = i+1; next;}
  if(i +1 > length(years)) break;
  data_temp1 <- df[which((df$YEAR == years[i] & df$WEEK >=40 & df$WEEK <= 52)), ]
  data_temp2 <- df[which((df$YEAR == years[i+1]& df$WEEK >= 1 & df$WEEK != 53)), ]
  data_temp <- rbind(data_temp1, data_temp2)
  data_temp$season <- paste(str_trim(toString(years[i])), "-", str_trim(toString(years[i+1] - 2000)), " season")
  data_total <- rbind(data_total, data_temp)
  i = i+1
}

national_baseline <- data.frame("WEEK" = df$WEEK, 'X..WEIGHTED.ILI' = 2.2, 'YEAR'=2019, 'season' = 'National Baseline' )
data_total <- rbind(data_total, national_baseline)


# Plotting the graph


plot <- ggplot(data=data_total, aes(x = data_total$WEEK, y = data_total$X..WEIGHTED.ILI,
                                    fill = data_total$season, shape = data_total$season,
                                    linetype = data_total$season))+
        geom_line(data=data_total, aes(color=data_total$season, group = data_total$season))+
        geom_point(data = data_total[which(data_total$season == '2018-19 season'), ])+
        scale_colour_manual(name = "", values = rainbow(8), guide = "legend")+
        scale_fill_manual(name = "", values = rainbow(8), guide = "legend")+
        scale_shape_manual(name = "", values = c(NA, NA, NA, NA, NA, NA,
                                                          24, NA), guide = "legend")+
        scale_linetype_manual(name = "", values = c("solid", "solid", "solid",
                                                     "solid", "solid", "solid", "solid", "dashed")
                                        , guide = "legend")+
        scale_y_continuous(limits = c(0, 8), breaks = seq(0, 8, by = 1),
                     name = "% of Visits for ILI", expand = c(0, 0))+
        scale_x_discrete("Week", breaks = levels(data_total$WEEK)[c(T,F)], expand = c(0, 0))+
        labs(title = "Percentage of visits for Influenza-like Illness (ILI) reported by the U.S. Outpatient Influenza-like Illness Surveillance Network (ILINet),
             \n Weekly National Summary, 2018-2019 and Selected Previous Seasons")+
        theme(axis.line = element_line(colour = "#000000", linetype = "solid",size = 0.4),
              legend.box.background = element_rect(fill = 'white', colour = "black", size=2),
        panel.background = element_rect(fill = 'white', colour = 'black'),
        plot.title = element_text(hjust=0.5, face="bold", size=14),
        axis.title.x = element_text(face="bold"),
        axis.title.y = element_text(face="bold"),
        legend.position = c(0.8,0.8))
plot


