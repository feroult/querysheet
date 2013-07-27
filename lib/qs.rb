class QS

  def api_map
    @api_map ||= {}
  end  
    
  def query_api(options)
    api_map[options[:api]] = options[:query] 
  end
  
  def execute_query(api)
  end
    
end