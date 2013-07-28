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
  
  def execute(api)
    execute_query(api_map[api])
  end
  
  def execute_query(query)
    PGHelper.query_as_json(query)
  end

  private :api_map
end