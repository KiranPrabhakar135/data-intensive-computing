server <-function(input, output, session) {
  
  observeEvent(input$Twitterbtn, {
    output$plot1 <- renderUI({
      tags$img(src = "OnlyTwitter.png")
      
    })
  })
  
  observeEvent(input$CDCbtn, {
    output$plot1 <- renderUI({
      tags$img(src = "CDCData.png")
    })
  })
  
  observeEvent(input$dropdown,{
    output$plot1 <- renderUI({
      if(input$dropdown == "keyword: all"){
        tags$img(src = "AllTwitterVsAllCDC.png",width = "900px", height = "550px")
      }
      else if(input$dropdown == "keyword: Flushot"){
        tags$img(src = "Compare_flushot_all.png", width = "900px", height = "550px")
      }
      else if(input$dropdown == "keyword: Influenza"){
        tags$img(src = "compare_influenza_all.png", width = "900px", height = "550px")
      }
    })
  })
  
}