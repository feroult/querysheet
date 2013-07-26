require 'test_helper'

class SetupTest < Test::Unit::TestCase
  def test_pg_connect

    conn = PG.connect( :host => ENV['QS_HOST'],
      :port => ENV['QS_PORT'],
      :dbname => ENV['QS_DBNAME'],
      :user => ENV['QS_USER'],
      :password => ENV['QS_PASSWORD'] )

    conn.exec( "SELECT now() as now" ) do |result|
      result.each do |row|
        assert_not_nil row.values_at('now')
      end
    end
  end

end