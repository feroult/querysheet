class QS
  def api_map
    @api_map ||= {}
  end
    
  def query_api(options)
    api_map[options[:api]] = options[:query]
  end

  def invalid?(api)
    !api_map.has_key?(api)
  end
  
  def execute_query(api)
  end
    
  private :api_map
end