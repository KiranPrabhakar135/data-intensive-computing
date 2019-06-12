ui <-pageWithSidebar(
  headerPanel('CSE587 Lab1'),
  sidebarPanel(
    actionButton('CDCbtn','CDC Map'),
    actionButton('Twitterbtn','Twitter Map'),
    selectInput('dropdown', 'CDC vs Twitter Maps:',
                c("keyword: all","keyword: Flushot", "keyword: Influenza"), selected = FALSE),
    width = 3
  ),
  mainPanel(
    uiOutput('plot1'),
    width = 
  )
)
